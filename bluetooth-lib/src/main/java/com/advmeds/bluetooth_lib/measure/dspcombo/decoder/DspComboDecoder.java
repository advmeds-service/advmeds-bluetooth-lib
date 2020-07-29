package com.advmeds.bluetooth_lib.measure.dspcombo.decoder;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;

import java.math.BigDecimal;

import timber.log.Timber;

public class DspComboDecoder implements BaseBtDataDecoder {
    @Override
    public String[] decode(byte[] receiveData) {
        String raw = new String(receiveData);

        Timber.d(raw);

        if (raw.length() != 19 || !raw.contains("DDCSZY")) {
            Timber.d("Raw data error");

            return null;
        }

        String result = raw.substring(7, 10);

        BigDecimal bd = new BigDecimal(result);

        BigDecimal bd2 = new BigDecimal("0.18");

        long answer = Math.round(bd.multiply(bd2).doubleValue());

        Timber.d("answer = %s", answer);

        return new String[] {Long.toString(answer)};
    }
}
