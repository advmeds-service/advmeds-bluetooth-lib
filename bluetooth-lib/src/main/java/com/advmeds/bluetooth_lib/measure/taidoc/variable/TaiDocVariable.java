package com.advmeds.bluetooth_lib.measure.taidoc.variable;

public interface TaiDocVariable {
    String getServicesUUID();

    byte[] getDataCommand();

    String getCharactersticUUID();

    byte[] getShutdownCommand();
}
