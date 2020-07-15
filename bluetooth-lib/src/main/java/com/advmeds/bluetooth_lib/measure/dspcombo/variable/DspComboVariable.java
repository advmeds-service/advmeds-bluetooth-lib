package com.advmeds.bluetooth_lib.measure.dspcombo.variable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DspComboVariable{
    private static String initialCommand = "CCDLJC$";

    private static String step2command = "CCHJJC$";

    private static String step3command = "CCSTAT$";

    private static String step4command = "CC JSOK$";

    private static String step5command = "CCJSOK$";

    public String getServicesUUID() {
        return "0000FFB0-0000-1000-8000-00805F9B34FB";
    }

    public String getNextStepCommand(String _preResponse) {
        if(_preResponse.contains("DDDLZT")) {
            return step2command;
        }
        else if(_preResponse.contains("DDHJER")) {
            return step3command;
        }
        else if(_preResponse.contains("CCFRSZ$")) {
            return step4command;
        }
        else if(_preResponse.contains("DDCSZY")) {
            return step5command;
        }
        else if(_preResponse.contains("DDCSZE")) {
            return step5command;
        }
        else if(_preResponse.contains("DDCSZS")) {
            return step5command;
        }

        else if(_preResponse.contains("DDFXCS")) {
            return step5command;
        }

        return "";
    }

    public String getInitialCommand() {
        return initialCommand;
    }

    public String getCharactersticUUID() {
        return "0000FFB2-0000-1000-8000-00805F9B34FB";
    }
}
