package com.advmeds.bluetooth_lib.measure;

public interface BaseBtDataDecoder {
    String[] decode(byte[] receiveData);
}
