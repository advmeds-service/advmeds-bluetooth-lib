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

    public GaomuConnection(GaomuVariable _variable) {
        variable = _variable;
    }

    public void startConnect(Context _context, BluetoothDevice bluetoothDevice, GaomuConnectionCallBack _callBack) {
        this.callBack = _callBack;

        bluetoothDevice.connectGatt(_context, false, this);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        BT_gatt = gatt;

        Timber.d("onConnectionStateChange : "+ status + " to " + newState);

        if(newState == BluetoothProfile.STATE_CONNECTED) {
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

        callBack.receiveData(characteristic.getValue());
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);

        Timber.d( "On Descriptor Write Status : "  + status);

        if(status == BluetoothGatt.GATT_SUCCESS) {
            gatt.setCharacteristicNotification(descriptor.getCharacteristic(),true);
        }
        else {
            disconnect(gatt);
        }
    }

    private void disconnect(BluetoothGatt _gatt) {
        if(servicesDiscoveredDisposable != null) {
            servicesDiscoveredDisposable.dispose();
        }

        _gatt.disconnect();

        _gatt.close();

        callBack.connectionDisconnect();
    }

    void disconnect() {
        if(servicesDiscoveredDisposable != null) {
            servicesDiscoveredDisposable.dispose();
        }

        if(BT_gatt != null) {
            BT_gatt.disconnect();

            BT_gatt.close();
        }
    }
}
