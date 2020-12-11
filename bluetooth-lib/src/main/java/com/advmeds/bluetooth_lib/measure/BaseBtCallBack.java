package com.advmeds.bluetooth_lib.measure;

public interface BaseBtCallBack {
    void onDeivceConnectFail();

    void onNoData();

    void onReceiveData(VitalSign vitalSign);

    void onDeviceConnected();

    void onMeasureFail(String message);
}
