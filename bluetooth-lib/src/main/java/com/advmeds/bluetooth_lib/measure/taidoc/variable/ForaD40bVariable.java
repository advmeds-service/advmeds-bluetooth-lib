package com.advmeds.bluetooth_lib.measure.taidoc.variable;

public class ForaD40bVariable {

    public byte[] getTimeCommand() {
        return new byte[]{(byte)0x51, (byte)0x25, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xA3, (byte)0x19};
    }
}
