package com.advmeds.bluetooth_lib.measure.taidoc.connection;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.advmeds.bluetooth_lib.measure.taidoc.variable.ForaD40bVariable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocVariable;

import timber.log.Timber;

public class ForaD40bConnection extends TaiDocConnection {
    private ForaD40bVariable foraD40bVariable = new ForaD40bVariable();

    private int data_type = 0;

    public ForaD40bConnection(TaiDocVariable _variable) {
        super(_variable);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        Timber.d( "onCharacteristicChanged : " + characteristic.getValue().length);

        for(int i = 0 ; i < characteristic.getValue().length ; i ++) {
//            Timber.d("Value?");
            Timber.d("Value" + characteristic.getValue()[i]);
        }

        if(characteristic.getValue().length >= 8) {
            if((characteristic.getValue()[1] & 0xFF) == 37) {
                Timber.d("Get Data Type");

                setDataType(characteristic.getValue());

                characteristic.setValue(variable.getDataCommand());

                gatt.writeCharacteristic(characteristic);
            } else {
                Timber.d("type = " + data_type);

                byte[] override_result = characteristic.getValue().clone();

                override_result[7] = (byte) data_type;

                callBack.receiveData(override_result);
            }
        }
    }
    @Override

    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        Timber.d( "On Descriptor Write Status : "  + status);

        if(status == BluetoothGatt.GATT_SUCCESS) {

            BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();

            characteristic.setValue(foraD40bVariable.getTimeCommand());

            gatt.writeCharacteristic(characteristic);
        }
        else {
            disconnect(gatt);
        }
    }

    private void setDataType(byte[] dataTypeArray) {
        data_type = (dataTypeArray[4] & 0xFF) >> 7;
    }
}
