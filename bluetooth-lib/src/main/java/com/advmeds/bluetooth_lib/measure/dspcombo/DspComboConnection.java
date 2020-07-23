package com.advmeds.bluetooth_lib.measure.dspcombo;


import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import com.advmeds.bluetooth_lib.measure.BaseConnection;
import com.advmeds.bluetooth_lib.measure.dspcombo.variable.DspComboVariable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DspComboConnection extends BaseConnection {
    private DspComboVariable variable;

    private Disposable servicesDiscoveredDisposable;

    private Disposable characteristicWriteDisposable;

    private Disposable descriptorWriteDisposable;

    public DspComboConnection(DspComboVariable _variable) {
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

//                    characteristic.setWriteType(BluetoothGattCharacteristic.);
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

        Timber.d("onCharacteristicChanged");

        Timber.d(new String(characteristic.getValue()));

        String response = new String(characteristic.getValue());

        if(response.contains("DDDLZT")) {
            execCharacteristicWrite(gatt, characteristic, variable.getNextStepCommand(response), 5);
        }
        else if(response.contains("DDHJER")) {
            execCharacteristicWrite(gatt, characteristic, variable.getNextStepCommand(response), 60);
        }
        else if(response.contains("CCFRSZ$")) {
            execCharacteristicWrite(gatt, characteristic, variable.getNextStepCommand(response), 150);
        }
        else if(response.contains("DDCSZY")) {
            stopCharacteristicWrite();

            callBack.receiveData(characteristic.getValue());
        }
        else if(response.contains("DDJSOK")) {

        }
        else {
            callBack.measuerFail();
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);

        Timber.d("onCharacteristicWrite");
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);

        Timber.d("onDescriptorWrite" + status);

        if(status == BluetoothGatt.GATT_SUCCESS) {
            if(descriptorWriteDisposable != null && !descriptorWriteDisposable.isDisposed()) {
                descriptorWriteDisposable.dispose();
            }

           execCharacteristicWrite(gatt, descriptor.getCharacteristic(), variable.getInitialCommand(), 5);
        }
    }

    private void stopCharacteristicWrite() {
        if(characteristicWriteDisposable != null && !characteristicWriteDisposable.isDisposed()) {
            characteristicWriteDisposable.dispose();
        }
    }

    private void execCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, String command, int period) {
        stopCharacteristicWrite();

        characteristic.setValue(command);

        Timber.d("writeCharacteristic : " + command);

        characteristicWriteDisposable =
                Observable.intervalRange(0, 3, 0 , period, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(
                                aLong -> {
                                    Timber.d("times : " + aLong);
                                    if(!allowConnect) {
                                        Timber.d("allowConnect4 = " + allowConnect);

                                        disconnect();

                                        return;
                                    }
                                    if(aLong == 2) {
                                        disconnect(gatt);
                                    }
                                    else if(aLong < 2){
                                        Observable.just(gatt.writeCharacteristic(characteristic)).subscribe();
                                    }
                                    else {
                                        characteristicWriteDisposable.dispose();
                                    }
                                },
                                throwable -> Timber.d(throwable)
                        );
    }
    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);

        Timber.d("onCharacteristicRead");
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
    }

    @Override
    public void shutdown() {
        if(BT_gatt != null) {
            disconnect();
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

        if(characteristicWriteDisposable != null) {
            characteristicWriteDisposable.dispose();
        }
    }

    @Override
    public void onDenyNotify() {
        if(descriptorWriteDisposable != null) {
            descriptorWriteDisposable.dispose();
        }

        if(characteristicWriteDisposable != null) {
            characteristicWriteDisposable.dispose();
        }
    }
}