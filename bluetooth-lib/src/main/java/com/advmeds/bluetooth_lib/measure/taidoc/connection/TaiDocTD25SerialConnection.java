package com.advmeds.bluetooth_lib.measure.taidoc.connection;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import com.advmeds.bluetooth_lib.measure.BaseConnection;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocVariable;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TaiDocTD25SerialConnection extends BaseConnection {
    protected TaiDocVariable variable;

    private Disposable servicesDiscoveredDisposable;

    private Disposable descriptorWriteDisposable;

    private ArrayList<Byte> receiveBuffer = new ArrayList<>();

    public TaiDocTD25SerialConnection(TaiDocVariable _variable) {
        variable = _variable;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        if (newState == BluetoothProfile.STATE_CONNECTED) {
            if(servicesDiscoveredDisposable != null && !servicesDiscoveredDisposable.isDisposed()) {
                servicesDiscoveredDisposable.dispose();
            }

            servicesDiscoveredDisposable =
                    Observable.intervalRange(0, 2, 1, 2, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(
                                    aLong -> {
                                        Timber.d("discoverServices :" + aLong);

                                        if(!allowConnect) {
                                            Timber.d("allowConnect2 = " + allowConnect);

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
                                    },
                                    throwable -> Timber.d(throwable)
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

                BluetoothGattDescriptor descriptor = characteristic.getDescriptors().get(0);

                if(gatt.setCharacteristicNotification(characteristic, true)) {
                    Timber.d("setCharacteristicNotification true");

                    if(descriptor == null
                            || !descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)) {
                        Timber.d("Write Descriptor Fail");

                        disconnect(gatt);
                    }
                    else {
                        if(descriptorWriteDisposable != null && !descriptorWriteDisposable.isDisposed()) {
                            descriptorWriteDisposable.dispose();
                        }

                        descriptorWriteDisposable =
                                Observable.intervalRange(0, 4, 0, 5, TimeUnit.SECONDS)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io())
                                        .subscribe(
                                                aLong -> {
                                                    receiveBuffer = new ArrayList<>();

                                                    Timber.d("writeDescriptor : " + aLong);
                                                    if(!allowConnect) {
                                                        Timber.d("allowConnect4 = " + allowConnect);

                                                        disconnect();

                                                        return;
                                                    }
                                                    if(aLong == 3) {
                                                        disconnect(gatt);
                                                    }
                                                    else if(aLong < 3){
                                                        Observable.just(gatt.writeDescriptor(descriptor)).subscribe();
                                                    }
                                                    else {
                                                        descriptorWriteDisposable.dispose();
                                                    }
                                                },
                                                throwable -> Timber.d(throwable)
                                        );
                    }
                }
                else {
                    disconnect(gatt);
                }
            }
            catch (Exception e) {
                disconnect(gatt);
            }
        } else {
            Timber.d("onServicesDiscovered received: " + status);

            disconnect(gatt);
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);

        Timber.d( "onCharacteristicChanged : " + characteristic.getValue().length);

        if(receiveBuffer.size() == 0) {
            if(characteristic.getValue().length > 3 && characteristic.getValue()[0] == 81) {
                for(int i = 0 ; i < characteristic.getValue().length ; i ++) {
                    Timber.d("Value" + characteristic.getValue()[i]);

                    receiveBuffer.add(characteristic.getValue()[i]);
                }
            }
        }
        else {
            for(int i = 0 ; i < characteristic.getValue().length ; i ++) {
                Timber.d("Value" + characteristic.getValue()[i]);

                receiveBuffer.add(characteristic.getValue()[i]);
            }

            if(receiveBuffer.size() >= (receiveBuffer.get(2)&0xFF) + 5) {
                byte[] result = new byte[(receiveBuffer.get(2)&0xFF) + 5];

                for(int i = 0; i<result.length; i++) {
                    result[i] = receiveBuffer.get(i);
                }

                receiveBuffer = new ArrayList<>();

                callBack.receiveData(result);
            }
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);

        Timber.d( "onCharacteristicWrite : "  + status);

        if(status != BluetoothGatt.GATT_SUCCESS) {
            disconnect(gatt);
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);

        Timber.d( "On Descriptor Write Status : "  + status);

        if(status == BluetoothGatt.GATT_SUCCESS) {

            BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();

            characteristic.setValue(variable.getDataCommand());

            gatt.writeCharacteristic(characteristic);
        }
        else {
            disconnect(gatt);
        }
    }

    @Override
    public void shutdown() {
        if(BT_gatt != null) {
            BluetoothGattService bluetoothGattService = BT_gatt.getService(UUID.fromString(variable.getServicesUUID()));

            BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristic(UUID.fromString(variable.getCharactersticUUID()));

            characteristic.setValue(variable.getShutdownCommand());

            BT_gatt.writeCharacteristic(characteristic);
        }
    }

    @Override
    public void onDisconnect() {
        if(servicesDiscoveredDisposable != null) {
            servicesDiscoveredDisposable.dispose();
        }

        if(descriptorWriteDisposable != null) {
            descriptorWriteDisposable.dispose();
        }
    }

    @Override
    public void onDenyNotify() {
        if(descriptorWriteDisposable != null) {
            descriptorWriteDisposable.dispose();
        }
    }
}
