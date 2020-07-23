package com.advmeds.bluetooth_lib.measure.taidoc;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.BaseBtDevice;
import com.advmeds.bluetooth_lib.measure.BaseConnection;
import com.advmeds.bluetooth_lib.measure.BaseConnectionCallBack;

public class TaiDocDevice extends BaseBtDevice implements BaseConnectionCallBack {
    private BaseConnection baseConnection;

    public TaiDocDevice(
            BaseBtDataDecoder _decoder
            , BaseConnection connection) {
        super(_decoder);

        baseConnection = connection;
    }

    @Override
    public void disconnect() {
        baseConnection.disconnect();
    }

    @Override
    public void startConnect(Context context, BluetoothDevice device) {
        baseConnection.disconnect();

        baseConnection.startConnect(context, device, this);
    }

    @Override
    public void receiveData(byte[] rawData) {
        String[] data = decoder.decode(rawData);

        if(data != null) {
            if(autoStopReceive) {
                if(baseConnection != null) {
                    baseConnection.setAllowNotify(false);
                }
            }
            if(autoShutdown) {
                if(baseConnection != null) {
                    baseConnection.shutdown();
                }
            }

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
    public void measuerFail() {

    }
}
