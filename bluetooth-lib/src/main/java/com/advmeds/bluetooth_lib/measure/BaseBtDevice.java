package com.advmeds.bluetooth_lib.measure;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public abstract class BaseBtDevice {
    protected BaseBtCallBack callBack;
    
    protected BaseBtDataDecoder decoder;

    protected boolean autoStopReceive = false;

    protected boolean autoShutdown = false;

    public BaseBtDevice(BaseBtDataDecoder _decoder) {
        decoder = _decoder;
    }

    public abstract void disconnect();

    public abstract void startConnect(Context context, BluetoothDevice device);

    public void setCallBack(BaseBtCallBack _callBack) {
        this.callBack = _callBack;
    }

    public BaseBtDevice setAutoStopReceive(boolean auto) {
        this.autoStopReceive = auto;

        return this;
    }

    public BaseBtDevice setShutdownAfterReceive(boolean auto_shutdown) {
        this.autoShutdown = auto_shutdown;

        return this;
    }
}
