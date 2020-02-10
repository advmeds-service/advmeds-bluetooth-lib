package com.advmeds.bluetooth_lib.measure.gaomu;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.advmeds.bluetooth_lib.measure.gaomu.variable.GaomuVariable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GaomuConnection extends BluetoothGattCallback {
    private GaomuVariable variable;

    BluetoothGatt BT_gatt;

    GaomuConnectionCallBack callBack;

    private Disposable servicesDiscoveredDisposable;

    private boolean allowConnect = false;

    public GaomuConnection(GaomuVariable _variable) {
        variable = _variable;
    }

    public void startConnect(Context _context, BluetoothDevice bluetoothDevice, GaomuConnectionCallBack _callBack) {
        this.callBack = _callBack;

        allowConnect = true;

        bluetoothDevice.connectGatt(_context, false, this);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        BT_gatt = gatt;

        Timber.d("onConnectionStateChange : "+ status + " to " + newState);

        if(newState == BluetoothProfile.STATE_CONNECTED) {
            if(!allowConnect) {
                Timber.d("allowConnect1 = " + allowConnect);

                disconnect();

                return;
            }

            if(servicesDiscoveredDisposable != null && !servicesDiscoveredDisposable.isDisposed()) {
                servicesDiscoveredDisposable.dispose();
            }

            servicesDiscoveredDisposable =
                    Observable.intervalRange(0, 2, 1, 2, TimeUnit.SECONDS)
                        .delay(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(
                            aLong -> {
                                Timber.d("discoverServices :" + aLong);

                                if(!allowConnect) {
                                    Timber.d("allowConnect1 = " + allowConnect);

                                    disconnect();

                                    return;
                                }

                                if(aLong == 1) {
                                    disconnect(gatt);
                                }
                                else if(aLong ==0){
                                    boolean result = gatt.discoverServices();
                                    Timber.d("discoverServices :" + result);
                                }
                                else {
                                    servicesDiscoveredDisposable.dispose();
                                }
                            }
                            , throwable -> Timber.d(throwable)
                    );
        }
        else if(newState == BluetoothProfile.STATE_CONNECTING) {
            Timber.d("Device Connecting...");
        }
        else {
            disconnect(gatt);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

        if(!allowConnect) {
            Timber.d("allowConnect1 = " + allowConnect);

            disconnect();

            return;
        }
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Timber.d("GATT_SUCCESS");

            servicesDiscoveredDisposable.dispose();

            try {
                BluetoothGattService bluetoothGattService = gatt.getService(UUID.fromString(variable.getServicesUUID()));

                BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristic(UUID.fromString(variable.getCharactersticUUID()));

                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(variable.getDescriptorUUID()));

                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

                if(!gatt.writeDescriptor(descriptor)) {
                    disconnect(gatt);
                }
            }
            catch (Exception e) {
                disconnect(gatt);
            }
        } else {
            disconnect(gatt);

            Timber.d("onServicesDiscovered received: " + status);
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);

        if(!allowConnect) {
            Timber.d("allowConnect1 = " + allowConnect);

            disconnect();

            return;
        }

        Timber.d("onCharacteristicChanged");

        callBack.receiveData(characteristic.getValue());
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);

        if(!allowConnect) {
            Timber.d("allowConnect1 = " + allowConnect);

            disconnect();

            return;
        }

        Timber.d( "On Descriptor Write Status : "  + status);

        if(status == BluetoothGatt.GATT_SUCCESS) {
            boolean connection_status = gatt.setCharacteristicNotification(descriptor.getCharacteristic(),true);

            if(connection_status) {
                callBack.onDeviceConnected();
            }
        }
        else {
            disconnect(gatt);
        }
    }



    private void disconnect(BluetoothGatt _gatt) {
        allowConnect = false;

        if(servicesDiscoveredDisposable != null) {
            servicesDiscoveredDisposable.dispose();
        }

        _gatt.disconnect();

        _gatt.close();

        allowConnect = false;

        callBack.connectionDisconnect();
    }

    void disconnect() {
        allowConnect = false;

        if(servicesDiscoveredDisposable != null) {
            servicesDiscoveredDisposable.dispose();
        }

        if(BT_gatt != null) {
            BT_gatt.disconnect();

            BT_gatt.close();
        }

        allowConnect = false;
    }
}
