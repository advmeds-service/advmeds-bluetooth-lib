package com.advmeds.bluetooth_lib.measure.dspcombo.decoder;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.VitalSign;

import java.math.BigDecimal;

import timber.log.Timber;

public class DspComboDecoder implements BaseBtDataDecoder {
    @Override
    public VitalSign decode(byte[] receiveData) {
        String raw = new String(receiveData);

        Timber.d(raw);

        if (raw.length() != 19 || !raw.contains("DDCSZY")) {
            Timber.d("Raw data error");

            return null;
        }

        String result = raw.substring(6, 9);

        if(result.startsWith("0")) {
            result = result.substring(1);
        }

        BigDecimal bd = new BigDecimal(result);

        BigDecimal bd2 = new BigDecimal("1.8");

        long answer = Math.round(bd.multiply(bd2).doubleValue());

        Timber.d("answer = %s", answer);

        VitalSign vs = new VitalSign();

        vs.setGlucose(Long.toString(answer));

        return vs;
    }
}
