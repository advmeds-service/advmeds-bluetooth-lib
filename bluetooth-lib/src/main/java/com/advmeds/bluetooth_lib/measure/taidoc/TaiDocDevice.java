package com.advmeds.bluetooth_lib.measure.taidoc;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.BaseBtDevice;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocVariable;

public class TaiDocDevice extends BaseBtDevice implements TaiDocConnectionCallBack {
    private TaiDocConnection taiDocConnection;

    public TaiDocDevice(
            BaseBtDataDecoder _decoder
            , TaiDocVariable _taiDocVariable) {
        super(_decoder);

        taiDocConnection = new TaiDocConnection(_taiDocVariable);
    }

    @Override
    public void disconnect() {
        taiDocConnection.disconnect();
    }

    @Override
    public void startConnect(Context context, BluetoothDevice device) {
        taiDocConnection.disconnect();

        taiDocConnection.startConnect(context, device, this);
    }

    @Override
    public void receiveData(byte[] rawData) {
        String[] data = decoder.decode(rawData);

        if(data != null) {
            callBack.onReceiveData(data);
        }
        else {
            callBack.onNoData();
        }
    }

    @Override
    public void connectionDisconnect() {
        callBack.onDeivceConnectFail();
    }
}
