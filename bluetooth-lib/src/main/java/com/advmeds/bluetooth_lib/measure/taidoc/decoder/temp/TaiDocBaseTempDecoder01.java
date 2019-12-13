package com.advmeds.bluetooth_lib.measure.taidoc.decoder.temp;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;

import timber.log.Timber;

/**
 * 適用機台:
 *     TD1241
 *     TAIDOC TD1261
 *     TAIDOC TD1035
 *     FORA IR40
 *     FORA IR42
 */
public class TaiDocBaseTempDecoder01 implements BaseBtDataDecoder {
    @Override
    public String[] decode(byte[] receiveData) {
        if(receiveData == null || receiveData.length != 8) {
            return null;
        }
        int originTemp = ((receiveData[3] * 256) + (receiveData[2] & 0xFF));

        Timber.d("%s", (receiveData[3] & 0xFF * 256));
        Timber.d("%s",  (receiveData[2] & 0xFF));

        if (originTemp > 0) {

            if(originTemp >= 1000) {
                double temp = (double) originTemp / 100;

                temp = Math.round(temp / .1) * .1; //四捨五入小數點第一位

                Timber.d("%s", temp);

                return new String[] {String.valueOf(temp)};
            }
            else {
                double temp = (double) originTemp / 10;

                Timber.d("%s", temp);

                return new String[] {String.valueOf(temp)};
            }
        }
        else {
            return null;
        }
    }
}
