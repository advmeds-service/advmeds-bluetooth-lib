package com.advmeds.bluetooth_lib.measure.taidoc;

public interface TaiDocConnectionCallBack {
    void receiveData(byte[] rawData);

    void connectionDisconnect();
}
