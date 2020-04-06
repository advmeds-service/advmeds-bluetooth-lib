package com.advmeds.bluetooth_lib.measure.taidoc.decoder.ua;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;

import timber.log.Timber;

/**
 * 適用機台:
 *      TAIDOC 4141(WITH METER)
 */
public class TaiDocBaseUADecoder01 implements BaseBtDataDecoder {
    @Override
    public String[] decode(byte[] receiveData) {
        if(receiveData != null
            && receiveData.length != 15) {

            return null;
        }

        double ua = (receiveData[12] & 0xFF);

        if(ua < 30
            || ua > 200) {
            return null;
        }

        return new String[] {String.valueOf(ua/10)};
    }
}
