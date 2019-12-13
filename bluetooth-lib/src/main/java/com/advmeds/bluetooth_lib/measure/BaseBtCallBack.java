package com.advmeds.bluetooth_lib.measure;

public interface BaseBtCallBack {
    void onDeivceConnectFail();

    void onNoData();

    void onReceiveData(String... values);
}
