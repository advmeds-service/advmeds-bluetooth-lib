package com.advmeds.bluetooth_lib.measure;

public interface BaseConnectionCallBack {
    void receiveData(byte[] rawData);

    void connectionDisconnect();

    void measuerFail();
}
