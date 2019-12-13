package com.advmeds.bluetooth_lib.measure.gaomu;

public interface GaomuConnectionCallBack {
    void receiveData(byte[] rawData);

    void connectionDisconnect();
}
