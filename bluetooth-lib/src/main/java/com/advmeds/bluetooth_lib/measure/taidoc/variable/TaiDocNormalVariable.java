package com.advmeds.bluetooth_lib.measure.taidoc.variable;

/**
 * 一般通用配置
 */
public class TaiDocNormalVariable implements TaiDocVariable {
    public String getServicesUUID() {
        return "00001523-1212-EFDE-1523-785FEABCD123";
    }

    public byte[] getDataCommand() {
        return new byte[]{(byte)0x51, (byte)0x26, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xA3, (byte)0x1A};
    }

    public String getCharactersticUUID() {
        return "00001524-1212-EFDE-1523-785FEABCD123";
    }

    public byte[] getShutdownCommand() {
        return new byte[]{(byte)0x51, (byte)0x50, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xA3, (byte)0x44};
    }
}
