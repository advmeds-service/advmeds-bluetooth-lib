package com.advmeds.bluetooth_lib.measure;


import com.advmeds.bluetooth_lib.measure.dspcombo.DspComboConnection;
import com.advmeds.bluetooth_lib.measure.dspcombo.DspComboDevice;
import com.advmeds.bluetooth_lib.measure.dspcombo.decoder.DspComboDecoder;
import com.advmeds.bluetooth_lib.measure.dspcombo.variable.DspComboVariable;
import com.advmeds.bluetooth_lib.measure.gaomu.GaomuDevice;
import com.advmeds.bluetooth_lib.measure.gaomu.decoder.GaomuBaseTempDecoder01;
import com.advmeds.bluetooth_lib.measure.gaomu.variable.GaomuNormalVariable;
import com.advmeds.bluetooth_lib.measure.taidoc.TaiDocDevice;
import com.advmeds.bluetooth_lib.measure.taidoc.connection.ForaD40bConnection;
import com.advmeds.bluetooth_lib.measure.taidoc.connection.TaiDocConnection;
import com.advmeds.bluetooth_lib.measure.taidoc.connection.TaiDocMeterConnection;
import com.advmeds.bluetooth_lib.measure.taidoc.connection.TaiDocTD25SerialConnection;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.bp.TaiDocBaseBpDecoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.bp.TaiDocBaseBpDecoder02;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.bp.TaiDocBaseBpDecoder03;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.mix.TaiDocMixDecoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.mix.TaiDocMixDecoder02;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.o2.TaiDocBaseO2Decoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.sugar.TaiDocBaseSugarDecoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.sugar.TaiDocBaseSugarDecoder02;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.td25serial.TaiDocTd25SerialDecoder;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.temp.TaiDocBaseTempDecoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.ua.TaiDocBaseUADecoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.decoder.weight.TaiDocBaseWeightDecoder01;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocMeterVariable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocNormalVariable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocTD2555Variable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocTD3140Variable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocTD8201Variable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaiDocTD8255Variable;
import com.advmeds.bluetooth_lib.measure.taidoc.variable.TaidocTD25SerialVariable;

public class BaseBtDeviceFactory {

    public static BaseBtDevice createBtDevice(String deviceName) {
        switch (deviceName) {
            case "FORA D40":
                return new TaiDocDevice(new TaiDocMixDecoder02()
                                        , new ForaD40bConnection(new TaiDocNormalVariable()));
            case "FORA P30 PLUS":
            case "TAIDOC TD3140":
                return new TaiDocDevice(new TaiDocBaseBpDecoder02()
                                        , new TaiDocConnection(new TaiDocTD3140Variable()));
            case "TAIDOC TD3128":
                return new TaiDocDevice(new TaiDocBaseBpDecoder01()
                                        , new TaiDocConnection(new TaiDocNormalVariable()));
            case "TAIDOC TD2555":
                return new TaiDocDevice(new TaiDocTd25SerialDecoder()
                        , new TaiDocTD25SerialConnection(new TaiDocTD2555Variable()));
            case "TAIDOC TD1241":
            case "TAIDOC TD1261":
            case "TAIDOC TD1035":
            case "FORA IR40":
            case "FORA IR42":
                return new TaiDocDevice(new TaiDocBaseTempDecoder01()
                                        , new TaiDocConnection(new TaiDocNormalVariable()));
            case "BF4030":
                return new GaomuDevice(new GaomuBaseTempDecoder01(), new GaomuNormalVariable());
            case "TAIDOC TD8201":
                return new TaiDocDevice(new TaiDocBaseO2Decoder01()
                                        , new TaiDocConnection(new TaiDocTD8201Variable()));
            case "TAIDOC TD8255":
                return new TaiDocDevice(new TaiDocBaseO2Decoder01()
                                        , new TaiDocConnection(new TaiDocTD8255Variable()));
            case "TAIDOC TD4257":
            case "FORA GD40":
                return new TaiDocDevice(new TaiDocBaseSugarDecoder01()
                                        , new TaiDocConnection(new TaiDocNormalVariable()));
//            case "TAIDOC TD4216":
//
//                return new TaiDocDevice(new TaiDocBaseSugarDecoder02()
//                                        , new TaiDocMeterConnection(new TaiDocMeterVariable()));
            case "TAIDOC TD4141":
            case "FORA MD6":
            case "TAIDOC TD4206":
            case "TAIDOC TD4216":
                return new TaiDocDevice(new TaiDocMixDecoder01()
                                        , new TaiDocConnection(new TaiDocNormalVariable()));
            case "FORA W600":
                return new TaiDocDevice(new TaiDocTd25SerialDecoder()
                                        , new TaiDocTD25SerialConnection(new TaidocTD25SerialVariable()));
            case "DSP Combo":
                return new DspComboDevice(new DspComboDecoder()
                        , new DspComboConnection(new DspComboVariable()));
        }

        return null;
    }
}
