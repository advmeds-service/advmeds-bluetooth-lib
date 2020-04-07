package com.advmeds.bluetooth_lib.measure.taidoc.variable;

public class TaiDocMeterVariable {
    public String getServicesUUID() {
        return "00001808-0000-1000-8000-00805f9b34fb";
    }

    public String getShutDownServicesUUID() {
        return "00001523-1212-EFDE-1523-785FEABCD123";
    }

    public byte[] getDataCommand() {
        return new byte[]{(byte)0x01, (byte)0x01};
    }

    public String getSendCharactersticUUID() {
        return "00002a52-0000-1000-8000-00805f9b34fb";
    }

    public String getReceiveCharactersticUUID() {
        return "00002a18-0000-1000-8000-00805f9b34fb";
    }

    public String getShutDownCharactersticUUID() {
        return "00001524-1212-EFDE-1523-785FEABCD123";
    }

    public byte[] getShutdownCommand() {
        return new byte[]{(byte)0x51, (byte)0x50, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xA3, (byte)0x44};
    }
}
