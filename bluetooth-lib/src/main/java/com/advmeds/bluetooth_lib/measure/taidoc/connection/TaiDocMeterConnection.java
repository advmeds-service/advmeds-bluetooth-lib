package com.advmeds.bluetooth_lib.measure.taidoc.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.advmeds.bluetooth_lib.measure.taidoc.TaiDocConnectionCallBack;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocMeterVariable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocVariable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TaiDocMeterConnection extends TaiDocBaseConnection {
    private TaiDocMeterVariable variable;

    private Disposable servicesDiscoveredDisposable;

    private Disposable descriptorWriteDisposable;

    public TaiDocMeterConnection(TaiDocMeterVariable _variable) {
        variable = _variable;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        if(newState == BluetoothProfile.STATE_CONNECTED) {
            if(servicesDiscoveredDisposable != null && !servicesDiscoveredDisposable.isDisposed()) {
                servicesDiscoveredDisposable.dispose();
            }

            servicesDiscoveredDisposable =
                    Observable.intervalRange(0, 2, 0, 2, TimeUnit.SECONDS)
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

            for(BluetoothGattService service : gatt.getServices()) {
                Timber.d(String.valueOf(service.getUuid()));
            }
            try {
                BluetoothGattService bluetoothGattService = gatt.getService(UUID.fromString(variable.getServicesUUID()));

                BluetoothGattCharacteristic sendCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString(variable.getSendCharactersticUUID()));

                BluetoothGattDescriptor sendDescriptor = sendCharacteristic.getDescriptors().get(0);

                BluetoothGattCharacteristic receiveCharacteristic = bluetoothGattService.getCharacteristic(UUID.fromString(variable.getReceiveCharactersticUUID()));

                BluetoothGattDescriptor receiveDescriptor = receiveCharacteristic.getDescriptors().get(0);

                if(gatt.setCharacteristicNotification(sendCharacteristic, true)
                    && gatt.setCharacteristicNotification(receiveCharacteristic, true)) {
                    Timber.d("setCharacteristicNotification true");



                    if((sendDescriptor == null || !sendDescriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE))
                            && (receiveDescriptor == null || !receiveDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE))) {
                        Timber.d("Write Descriptor Fail");

                        disconnect(gatt);
                    }
                    else {
                        if(descriptorWriteDisposable != null && !descriptorWriteDisposable.isDisposed()) {
                            descriptorWriteDisposable.dispose();
                        }

                        descriptorWriteDisposable =
                                Observable.intervalRange(0, 4, 1, 5, TimeUnit.SECONDS)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.io())
                                        .subscribe(
                                                aLong -> {
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
                                                        Observable.just(gatt.writeDescriptor(sendDescriptor)).subscribe();
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

        for(int i = 0 ; i < characteristic.getValue().length ; i ++) {
//            Timber.d("" + characteristic.getValue()[i]);
            Timber.d("" + (characteristic.getValue()[i] & 0xFF));
        }

        callBack.receiveData(characteristic.getValue());
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
            if(descriptorWriteDisposable != null) {
                descriptorWriteDisposable.dispose();
            }

            if(descriptor.getCharacteristic().getUuid().toString().equals(variable.getSendCharactersticUUID())) {
                BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();

                characteristic.setValue(variable.getDataCommand());

                gatt.writeCharacteristic(characteristic);
            }
            else if(descriptor.getCharacteristic().getUuid().toString().equals(variable.getReceiveCharactersticUUID())) {
                BluetoothGattService bluetoothGattService = gatt.getService(UUID.fromString(variable.getServicesUUID()));

                BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristic(UUID.fromString(variable.getSendCharactersticUUID()));

                BluetoothGattDescriptor sendDescriptor = characteristic.getDescriptors().get(0);

                if(!sendDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                        || !gatt.writeDescriptor(sendDescriptor)) {
                    disconnect(gatt);
                }
            }
        }
        else {
            disconnect(gatt);
        }
    }

    @Override
    public void shutdown() {
        if(BT_gatt != null) {
            BluetoothGattService bluetoothGattService = BT_gatt.getService(UUID.fromString(variable.getShutDownServicesUUID()));

            BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristic(UUID.fromString(variable.getShutDownCharactersticUUID()));

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
