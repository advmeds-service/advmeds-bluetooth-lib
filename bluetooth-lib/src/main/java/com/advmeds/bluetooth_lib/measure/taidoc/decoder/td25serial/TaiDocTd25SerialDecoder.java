package com.advmeds.bluetooth_lib.measure.taidoc.decoder.td25serial;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.VitalSign;

/**
 * 適用機台:
 *      TD25系列
 */
public class TaiDocTd25SerialDecoder implements BaseBtDataDecoder {
    @Override
    public VitalSign decode(byte[] receiveData) {
        if(receiveData == null || receiveData.length != 40) {
            return null;
        }
        Double weight = (((receiveData[16] & 0xFF) * 256) + (receiveData[17] & 0xFF)) / 10.0;

        VitalSign vs = new VitalSign();

        vs.setWeight(String.valueOf(weight));

        return vs;
    }
}
