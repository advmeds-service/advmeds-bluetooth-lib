package com.advmeds.bluetooth_lib.measure.taidoc.decoder.weight;


import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;

import timber.log.Timber;

/**
 * 適用機台:
 *     TD2555
 */
public class TaiDocBaseWeightDecoder01 implements BaseBtDataDecoder {
    private int receiveDataCount = 0;

    @Override
    public String[] decode(byte[] receiveData) {
        Timber.d("decode");

        if(receiveData == null
                || receiveData.length != 8) {
            receiveDataCount = 0;

            return null;
        }
        else {
            receiveDataCount ++;
        }

        if(receiveDataCount == 3) {
            String weightBuf = Integer.toHexString(receiveData[1] & 0xFF);

            weightBuf = (receiveData[0] & 0xFF) + weightBuf; //是字串相連不是數值相加

            double weight = Integer.valueOf(weightBuf, 16).doubleValue() / 10;

            weight = Math.round(weight / .1) * .1; //四捨五入小數點第一位

            return new String[] {String.valueOf(weight)};
        }
        else {
            return null;
        }

    }
}
