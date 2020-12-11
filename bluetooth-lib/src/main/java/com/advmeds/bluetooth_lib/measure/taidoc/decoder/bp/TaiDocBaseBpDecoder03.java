package com.advmeds.bluetooth_lib.measure.taidoc.decoder.bp;


import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.VitalSign;

/**
 * 適用機台:
 *      FORA D40
 */
public class TaiDocBaseBpDecoder03 implements BaseBtDataDecoder {
    @Override
    public VitalSign decode(byte[] receiveData) {
        if(receiveData == null
                || receiveData.length != 8
                || receiveData[2] == 0
                || receiveData[5] == 0) {
            return null;
        }
        VitalSign vs = new VitalSign();

        vs.setSystolic(String.valueOf((receiveData[2] & 0xFF)  + ((receiveData[3] & 0xFF) * 256)));

        vs.setDiastolic(String.valueOf(receiveData[4] & 0xFF));

        vs.setPulse(String.valueOf(receiveData[5] & 0xFF));

        return vs;
    }
}