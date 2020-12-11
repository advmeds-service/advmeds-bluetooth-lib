package com.advmeds.bluetooth_lib.measure;

public interface BaseBtDataDecoder {
    VitalSign decode(byte[] receiveData);
}
