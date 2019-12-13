package com.advmeds.bluetooth_lib.measure.taidoc.decoder.bp;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;

/**
 * 適用機台:
 *      TD3128
 *      FORA P30 PLUS
 */
public class TaiDocBaseBpDecoder01 implements BaseBtDataDecoder {
    @Override
    public String[] decode(byte[] receiveData) {
        if(receiveData == null
                || receiveData.length != 8
                || receiveData[2] == 0
                || receiveData[5] == 0
                || receiveData[5] <= 3) {
            return null;
        }

        String sbp = String.valueOf(receiveData[2] & 0xFF);

        String dbp = String.valueOf(receiveData[4] & 0xFF);

        String pulse = String.valueOf(receiveData[5] & 0xFF);

        return new String[] {sbp, dbp, pulse};
    }
}
