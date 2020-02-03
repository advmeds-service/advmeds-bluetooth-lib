package com.advmeds.bluetooth_lib.measure.gaomu;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.BaseBtDevice;
import com.advmeds.bluetooth_lib.measure.gaomu.variable.GaomuVariable;

public class GaomuDevice extends BaseBtDevice implements GaomuConnectionCallBack {
    private GaomuConnection gaomuConnection;

    public GaomuDevice(BaseBtDataDecoder _decoder, GaomuVariable variable) {
        super(_decoder);

        gaomuConnection = new GaomuConnection(variable);
    }

    @Override
    public void disconnect() {
        gaomuConnection.disconnect();
    }

    @Override
    public void startConnect(Context context, BluetoothDevice device) {
        gaomuConnection.disconnect();

        gaomuConnection.startConnect(context, device, this);
    }

    @Override
    public void receiveData(byte[] rawData) {
        String[] data = decoder.decode(rawData);

        if(data != null && data.length > 0) {
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

    @Override
    public void onDeviceConnected() {
        callBack.onDeviceConnected();
    }
}
