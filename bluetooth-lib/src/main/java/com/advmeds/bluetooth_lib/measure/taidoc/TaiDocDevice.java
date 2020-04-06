package com.advmeds.bluetooth_lib.measure.taidoc;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.BaseBtDevice;
import com.advmeds.bluetooth_lib.measure.taidoc.connection.TaiDocBaseConnection;
import com.advmeds.bluetooth_lib.measure.taidoc.connection.TaiDocConnection;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocVariable;

public class TaiDocDevice extends BaseBtDevice implements TaiDocConnectionCallBack {
    private TaiDocBaseConnection taiDocBaseConnection;

    public TaiDocDevice(
            BaseBtDataDecoder _decoder
            , TaiDocBaseConnection connection) {
        super(_decoder);

        taiDocBaseConnection = connection;
    }

    @Override
    public void disconnect() {
        taiDocBaseConnection.disconnect();
    }

    @Override
    public void startConnect(Context context, BluetoothDevice device) {
        taiDocBaseConnection.disconnect();

        taiDocBaseConnection.startConnect(context, device, this);
    }

    @Override
    public void receiveData(byte[] rawData) {
        String[] data = decoder.decode(rawData);

        if(data != null) {
            if(autoStopReceive) {
                if(taiDocBaseConnection != null) {
                    taiDocBaseConnection.setAllowNotify(false);
                }
            }
            if(autoShutdown) {
                if(taiDocBaseConnection != null) {
                    taiDocBaseConnection.shutdown();
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
}
