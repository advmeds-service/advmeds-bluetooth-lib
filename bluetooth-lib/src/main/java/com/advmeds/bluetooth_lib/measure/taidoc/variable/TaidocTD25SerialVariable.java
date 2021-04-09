package com.advmeds.bluetooth_lib.measure.taidoc.variable;

public class TaidocTD25SerialVariable implements TaiDocVariable{
    @Override
    public String getServicesUUID() {
        return "00001523-1212-EFDE-1523-785FEABCD123";
    }

    @Override
    public byte[] getDataCommand() {
        return new byte[]{(byte)0x51, (byte)0x77, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0xA3, (byte)0x6D};
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
