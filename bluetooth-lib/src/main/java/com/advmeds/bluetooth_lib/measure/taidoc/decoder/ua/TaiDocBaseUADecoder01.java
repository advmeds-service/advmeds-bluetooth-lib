package com.advmeds.bluetooth_lib.measure.taidoc.decoder.ua;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.VitalSign;

import timber.log.Timber;

/**
 * 適用機台:
 */
public class TaiDocBaseUADecoder01 implements BaseBtDataDecoder {
    @Override
    public VitalSign decode(byte[] receiveData) {
        if(receiveData != null
            && receiveData.length != 15) {

            return null;
        }

        double ua = (receiveData[12] & 0xFF);

        if(ua < 30
            || ua > 200) {
            return null;
        }
        VitalSign vs = new VitalSign();

        vs.setUricAcid(String.valueOf(ua/10));

        return vs;
    }
}
