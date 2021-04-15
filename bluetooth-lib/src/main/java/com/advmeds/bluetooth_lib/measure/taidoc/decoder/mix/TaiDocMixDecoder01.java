package com.advmeds.bluetooth_lib.measure.taidoc.decoder.mix;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.VitalSign;

import java.math.BigDecimal;

import timber.log.Timber;

/**
 * 適用機台:
 *      FORA MD6
 *      TAIDOC 4141(WITH METER)
 *      TAIDOC 4216
 *      TAIDOC 4206
 */
public class TaiDocMixDecoder01 implements BaseBtDataDecoder {
    @Override
    public VitalSign decode(byte[] receiveData) {
        if(receiveData == null
                || receiveData.length != 8
                || receiveData[2] == 0) {
            return null;
        }

        int type = ((receiveData[5] << 2)) & 0xFF;

        type = type >> 4;

        String value = String.valueOf((receiveData[2] & 0xFF) + ((receiveData[3] & 0xFF) * 256));

        VitalSign vs = new VitalSign();

        BigDecimal b1 = new BigDecimal(value);

        BigDecimal b2;

        Timber.d("type : " + type);
        switch (type) {
            case 0: // 血糖
                vs.setGlucose(value);
                break;
            case 6: // 紅血球容積比(HC)
                vs.setHematocrit(value);
                break;
            case 7: // 血酮
                b2 = new BigDecimal("30");

                vs.setKetone(b1.divide(b2,1, BigDecimal.ROUND_DOWN).setScale(1, BigDecimal.ROUND_DOWN).toString());
                break;
            case 8: // 尿酸
                b2 = new BigDecimal("0.1");

                vs.setUricAcid(b1.multiply(b2).toString());
                break;
            case 9: // 膽固醇
                vs.setCholesterol(value);
                break;
            case 11: // 血紅素(HB)
                b2 = new BigDecimal("0.1");

                vs.setHemoglobin(b1.multiply(b2).toString());
                break;
            case 12: // 血中乳酸
                b2 = new BigDecimal("9.008");

                vs.setLactate(b1.divide(b2,1, BigDecimal.ROUND_DOWN).setScale(1, BigDecimal.ROUND_DOWN).toString());
                break;
        }

        return vs;
    }
}
