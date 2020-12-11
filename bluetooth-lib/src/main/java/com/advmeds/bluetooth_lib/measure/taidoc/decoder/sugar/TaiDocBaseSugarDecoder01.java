package com.advmeds.bluetooth_lib.measure.taidoc.decoder.sugar;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.VitalSign;

/**
 * 適用機台:
 *      TD4257
 *      FORA GD40
 */
public class TaiDocBaseSugarDecoder01 implements BaseBtDataDecoder {
    @Override
    public VitalSign decode(byte[] receiveData) {
        if(receiveData == null
                || receiveData.length != 8
                || receiveData[2] == 0) {
            return null;
        }

        VitalSign vs = new VitalSign();

        vs.setGlucose(String.valueOf((receiveData[2] & 0xFF) + ((receiveData[3] & 0xFF) * 256)));

        return vs;
    }
}
