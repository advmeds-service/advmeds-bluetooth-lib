package com.advmeds.bluetooth_lib.measure.gaomu.variable;

public interface GaomuVariable {
    String getServicesUUID();

    byte[] getDataCommand();

    String getCharactersticUUID();

    String getDescriptorUUID();
}
