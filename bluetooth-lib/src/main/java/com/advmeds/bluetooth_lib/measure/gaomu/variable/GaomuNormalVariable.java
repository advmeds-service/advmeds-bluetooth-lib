package com.advmeds.bluetooth_lib.measure.gaomu.variable;

public class GaomuNormalVariable implements GaomuVariable {
    @Override
    public String getServicesUUID() {
        return "000018f0-0000-1000-8000-00805f9b34fb";
    }

    @Override
    public byte[] getDataCommand() {
        return new byte[0];
    }

    @Override
    public String getCharactersticUUID() {
        return "00002af0-0000-1000-8000-00805f9b34fb";
    }

    @Override
    public String getDescriptorUUID() {
        return "00002902-0000-1000-8000-00805f9b34fb";
    }
}
