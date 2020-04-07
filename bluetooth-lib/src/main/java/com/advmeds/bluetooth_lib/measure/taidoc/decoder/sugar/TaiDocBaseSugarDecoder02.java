package com.advmeds.bluetooth_lib.measure.taidoc.decoder.sugar;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;

import timber.log.Timber;

/**
 * 適用機台:
 *      TAIDOC TD4206(WITH METER)
 *      TAIDOC TD4216
 */
public class TaiDocBaseSugarDecoder02 implements BaseBtDataDecoder {
    @Override
    public String[] decode(byte[] receiveData) {
        if(receiveData != null
                && receiveData.length != 15) {

            return null;
        }

        int sugar = (receiveData[12] & 0xFF);

        if(sugar < 4
                || sugar >= 255) {
            return null;
        }

        Timber.d("Decode Success");

        return new String[] {String.valueOf(sugar)};
    }
}
