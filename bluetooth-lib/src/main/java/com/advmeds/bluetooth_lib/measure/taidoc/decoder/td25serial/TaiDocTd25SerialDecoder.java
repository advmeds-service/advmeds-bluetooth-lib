package com.advmeds.bluetooth_lib.measure.taidoc.decoder.td25serial;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.VitalSign;

import java.math.BigDecimal;

import timber.log.Timber;

/**
 * 適用機台:
 *      TD25系列
 */
public class TaiDocTd25SerialDecoder implements BaseBtDataDecoder {
    @Override
    public VitalSign decode(byte[] receiveData) {
        if(receiveData == null || receiveData.length < 32) {
            return null;
        }
        Double weight = (((receiveData[16] & 0xFF) * 256) + (receiveData[17] & 0xFF)) / 10.0;

        int height = receiveData[11] & 0xFF;

        VitalSign vs = new VitalSign();

        vs.setWeight(String.valueOf(weight));

        vs.setHeight(String.valueOf(height));

        if(receiveData.length == 40) {
            BigDecimal bmi = new BigDecimal(((receiveData[20] & 0xFF) * 256) + (receiveData[21] & 0xFF)).multiply(new BigDecimal("0.1"));

            int bmr = ((receiveData[22] & 0xFF) * 256) + (receiveData[23] & 0xFF);

            BigDecimal bf = new BigDecimal(((receiveData[24] & 0xFF) * 256) + (receiveData[25] & 0xFF)).multiply(new BigDecimal("0.1"));

            BigDecimal bm = new BigDecimal(((receiveData[26] & 0xFF) * 256) + (receiveData[27] & 0xFF)).multiply(new BigDecimal("0.1"));

            BigDecimal bn = new BigDecimal(receiveData[28] & 0xFF).multiply(new BigDecimal("0.1"));

            BigDecimal bw = new BigDecimal(((receiveData[29] & 0xFF) * 256) + (receiveData[30] & 0xFF)).multiply(new BigDecimal("0.1"));

            BigDecimal vfr = new BigDecimal(receiveData[32] & 0xFF).multiply(new BigDecimal("0.1"));

            int amr = ((receiveData[34] & 0xFF) * 256) + (receiveData[35] & 0xFF);

            //Metabolic Age
            int mAge = (receiveData[36] & 0xFF);

            if(bmi.doubleValue() > 0) { vs.setBmi(bmi.toString()); }
            if(bmr > 0) { vs.setBmr(String.valueOf(bmr)); }
            if(bf.doubleValue() > 0) { vs.setBodyFat(bf.toString()); }
            if(bm.doubleValue() > 0) { vs.setBodyMuscle(bm.toString()); }
            if(bn.doubleValue() > 0) { vs.setBodyBone(bn.toString()); }
            if(bw.doubleValue() > 0) { vs.setBodyWater(bw.toString()); }
            if(vfr.doubleValue() > 0) { vs.setVisceralFatRating(vfr.toString()); }
            if(amr > 0) { vs.setAmr(String.valueOf(amr)); }
            if(mAge > 0) { vs.setMetabolicAge(String.valueOf(mAge)); }
        }

        return vs;
    }
}
