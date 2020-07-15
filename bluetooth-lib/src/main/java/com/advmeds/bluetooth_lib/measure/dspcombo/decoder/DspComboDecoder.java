package com.advmeds.bluetooth_lib.measure.dspcombo.decoder;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;

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

        String result = raw.substring(6, 9);

        if(result.startsWith("0")) {
            result = result.substring(1);
        }

        Timber.d(result);

        return new String[] {result};
    }
}