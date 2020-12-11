package com.advmeds.bluetooth_lib.measure.taidoc.decoder.mix;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.VitalSign;

import java.math.BigDecimal;

import timber.log.Timber;

/**
 * 適用機台:
 *      FORA D40B
 */
public class TaiDocMixDecoder02 implements BaseBtDataDecoder {
    @Override
    public VitalSign decode(byte[] receiveData) {
            if(receiveData == null
                    || receiveData.length != 8) {
                return null;
            }

            int type = receiveData[7] & 0xFF;

            String value = String.valueOf((receiveData[2] & 0xFF) + ((receiveData[3] & 0xFF) * 256));

            VitalSign vs = new VitalSign();

            Timber.d("type : " + type);
            switch (type) {
                case 0: // 血糖
                    vs.setGlucose(value);
                    break;
                case 1: // 紅血球容積比(HC)
                    vs.setSystolic(value);

                    vs.setDiastolic(String.valueOf(receiveData[4] & 0xFF));

                    vs.setPulse(String.valueOf(receiveData[5] & 0xFF));

                    break;
            }

            return vs;
    }
}
