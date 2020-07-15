package com.advmeds.bluetooth_lib.measure;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

import com.advmeds.bluetooth_lib.measure.taidoc.TaiDocConnectionCallBack;

import timber.log.Timber;

abstract public class BaseConnection  extends BluetoothGattCallback {
    protected BluetoothGatt BT_gatt;

    protected BaseConnectionCallBack callBack;

    protected boolean allowConnect = false; //是否允許繼續連線

    protected boolean allowNotify = true; //是否允許繼續接收數據變動

    public void startConnect(Context _context, BluetoothDevice bluetoothDevice, BaseConnectionCallBack _callBack) {
        this.callBack = _callBack;

        allowConnect = true;

        allowNotify = true;

        bluetoothDevice.connectGatt(_context, false, this);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        BT_gatt = gatt;

        Timber.d("onConnectionStateChange : "+ status + " to " + newState);

        if(!allowConnect) {
            Timber.d("allowConnect1 = " + allowConnect);

            disconnect();

            throw new RuntimeException("Not allow connect");
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

        if(!allowConnect) {
            Timber.d("allowConnect3 = " + allowConnect);

            disconnect();

            throw new RuntimeException("Not allow connect");
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic){
        super.onCharacteristicChanged(gatt, characteristic);

        if(!allowConnect) {
            Timber.d("allowConnect5 = " + allowConnect);

            disconnect();

            throw new RuntimeException("Not allow connect");
        }

        if(!allowNotify) {
            Timber.d("allowNotify = " + allowNotify);

            throw new RuntimeException("Not allow notify");
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);

        if(!allowConnect) {
            Timber.d("allowConnect6 = " + allowConnect);

            disconnect();

            throw new RuntimeException("Not allow connect");
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);

        if(!allowConnect) {
            Timber.d("allowConnect7 = " + allowConnect);

            disconnect();

            throw new RuntimeException("Not allow connect");
        }
    }

    public void setAllowNotify(boolean allow) {
        allowNotify = allow;

        if(!allow) {
            onDenyNotify();
        }
    }

    /**
     * 內部調用專用的disconnect
     * @param _gatt
     */
    protected void disconnect(BluetoothGatt _gatt) {
        allowConnect = false;

        _gatt.disconnect();

        _gatt.close();

        allowConnect = false;

        onDisconnect();

        callBack.connectionDisconnect();
    }

    /**
     * 外部調用專用的disconnect
     */
    public void disconnect() {
        allowConnect = false;

        if(BT_gatt != null) {
            BT_gatt.disconnect();

            BT_gatt.close();
        }

        allowConnect = false;

        onDisconnect();
    }

    abstract public void onDenyNotify();

    abstract public void shutdown();

    abstract public void onDisconnect();
}

