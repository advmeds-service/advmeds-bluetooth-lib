package com.advmeds.bluetooth_lib.measure;


import com.advmeds.bluetooth_lib.measure.gaomu.GaomuDevice;
import com.advmeds.bluetooth_lib.measure.gaomu.decoder.GaomuBaseTempDecoder01;
import com.advmeds.bluetooth_lib.measure.gaomu.variable.GaomuNormalVariable;
import com.advmeds.bluetooth_lib.measure.taidoc.TaiDocDevice;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.bp.TaiDocBaseBpDecoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.bp.TaiDocBaseBpDecoder02;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.bp.TaiDocBaseBpDecoder03;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.o2.TaiDocBaseO2Decoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.sugar.TaiDocBaseSugarDecoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.temp.TaiDocBaseTempDecoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.weight.TaiDocBaseWeightDecoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocNormalVariable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocTD2555Variable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocTD3140Variable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocTD8201Variable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocTD8255Variable;

public class BaseBtDeviceFactory {

    public static BaseBtDevice createBtDevice(String deviceName) {
        switch (deviceName) {
            case "FORA D40":
                return new TaiDocDevice(new TaiDocBaseBpDecoder03(), new TaiDocNormalVariable());
            case "FORA P30 PLUS":
            case "TAIDOC TD3140":
                return new TaiDocDevice(new TaiDocBaseBpDecoder02(), new TaiDocTD3140Variable());
            case "TAIDOC TD3128":
                return new TaiDocDevice(new TaiDocBaseBpDecoder01(), new TaiDocNormalVariable());
            case "TAIDOC TD2555":
                return new TaiDocDevice(new TaiDocBaseWeightDecoder01(), new TaiDocTD2555Variable());
            case "TAIDOC TD1241":
            case "TAIDOC TD1261":
            case "TAIDOC TD1035":
            case "FORA IR40":
            case "FORA IR42":
                return new TaiDocDevice(new TaiDocBaseTempDecoder01(), new TaiDocNormalVariable());
            case "BF4030":
                return new GaomuDevice(new GaomuBaseTempDecoder01(), new GaomuNormalVariable());
            case "TAIDOC TD8201":
                return new TaiDocDevice(new TaiDocBaseO2Decoder01(), new TaiDocTD8201Variable());
            case "TAIDOC TD8255":
                return new TaiDocDevice(new TaiDocBaseO2Decoder01(), new TaiDocTD8255Variable());
            case "TAIDOC TD4257":
            case "FORA GD40":
                return new TaiDocDevice(new TaiDocBaseSugarDecoder01(), new TaiDocNormalVariable());
        }

        return null;
    }
}
