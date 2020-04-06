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
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocVariable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

abstract public class TaiDocBaseConnection extends BluetoothGattCallback {
    protected BluetoothGatt BT_gatt;

    protected TaiDocConnectionCallBack callBack;

    protected boolean allowConnect = false; //是否允許繼續連線

    protected boolean allowNotify = true; //是否允許繼續接收數據變動

    public void startConnect(Context _context, BluetoothDevice bluetoothDevice, TaiDocConnectionCallBack _callBack) {
        this.callBack = _callBack;

        allowConnect = true;

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

            return;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

        if(!allowConnect) {
            Timber.d("allowConnect3 = " + allowConnect);

            disconnect();

            return;
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);

        if(!allowConnect) {
            Timber.d("allowConnect5 = " + allowConnect);

            disconnect();

            return;
        }

        if(!allowNotify) {
            Timber.d("allowNotify = " + allowNotify);

            return;
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);

        if(!allowConnect) {
            Timber.d("allowConnect6 = " + allowConnect);

            disconnect();

            return;
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);

        if(!allowConnect) {
            Timber.d("allowConnect7 = " + allowConnect);

            disconnect();

            return;
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

