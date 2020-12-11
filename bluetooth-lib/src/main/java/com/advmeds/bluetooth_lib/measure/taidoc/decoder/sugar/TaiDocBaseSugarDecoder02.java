package com.advmeds.bluetooth_lib.measure.taidoc.decoder.sugar;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.VitalSign;

import timber.log.Timber;

/**
 * 適用機台:
 */
public class TaiDocBaseSugarDecoder02 implements BaseBtDataDecoder {
    @Override
    public VitalSign decode(byte[] receiveData) {
        if(receiveData != null
                && receiveData.length != 15) {

            return null;
        }

        int sugar = (receiveData[12] & 0xFF) + ((receiveData[11] & 0xFF) * 256);

        if(sugar < 4) {
            return null;
        }

        Timber.d("Decode Success");

        VitalSign vs = new VitalSign();

        vs.setGlucose(String.valueOf(sugar));

        return vs;
    }
}
