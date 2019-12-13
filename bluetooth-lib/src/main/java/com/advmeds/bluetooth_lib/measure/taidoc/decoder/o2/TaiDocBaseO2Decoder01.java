package com.advmeds.bluetooth_lib.measure.taidoc.decoder.o2;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;

/**
 * 適用機台:
 *      TD8201
 *      TD8255
 */
public class TaiDocBaseO2Decoder01 implements BaseBtDataDecoder {
    @Override
    public String[] decode(byte[] receiveData) {
        if(receiveData == null
                || receiveData.length != 8
                || receiveData[2] == 0
                || receiveData[5] == 0) {
            return null;
        }

        String o2 = String.valueOf(receiveData[2] & 0xFF);

        return new String[] {o2};
    }
}
