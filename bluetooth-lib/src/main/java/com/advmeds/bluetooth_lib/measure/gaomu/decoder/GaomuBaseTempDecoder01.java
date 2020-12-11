package com.advmeds.bluetooth_lib.measure.gaomu.decoder;

import com.advmeds.bluetooth_lib.measure.BaseBtDataDecoder;
import com.advmeds.bluetooth_lib.measure.VitalSign;

public class GaomuBaseTempDecoder01 implements BaseBtDataDecoder {
    @Override
    public VitalSign decode(byte[] receiveData) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[receiveData.length * 2];

        for (int j = 0; j < receiveData.length; j++) {
            int v = receiveData[j] & 0xFF;

            hexChars[j * 2] = hexArray[v >>> 4];

            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String HexString = new String(hexChars);

        double DValue = 0;

        if (HexString.length() > 6) {
            String realString = HexString.substring(2, 6);

            int INTValue = Integer.parseInt(realString, 16);

            DValue = (double) INTValue / 10;
        }

        VitalSign vs = new VitalSign();

        vs.setTemperature(String.valueOf(DValue));

        return vs;
    }
}
