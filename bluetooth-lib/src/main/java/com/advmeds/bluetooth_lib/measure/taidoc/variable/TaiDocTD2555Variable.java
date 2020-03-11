package com.advmeds.bluetooth_lib.measure.taidoc.variable;

public class TaiDocTD2555Variable implements TaiDocVariable {
    @Override
    public String getServicesUUID() {
        return "00001523-1212-EFDE-1523-785FEABCD123";
    }

    @Override
    public byte[] getDataCommand() {
        return new byte[]{(byte)0x51, (byte)0x71, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0xA3, (byte)0x67};
    }

    @Override
    public String getCharactersticUUID() {
        return "00001524-1212-EFDE-1523-785FEABCD123";
    }

    @Override
    public byte[] getShutdownCommand() {
        return new byte[]{(byte)0x51, (byte)0x50, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xA3, (byte)0x44};
    }
}
