package com.obdsim.persistence.entities;

import android.text.TextUtils;

public class MassAirFlowCommand extends FuelLevelCommand {
    public MassAirFlowCommand() {
        super("0110", "41 10 64 64>", "Velocidad del flujo del aire", true);
    }

    public MassAirFlowCommand(String cmd, String response, String desc, Boolean isState) {
        super(cmd, response, desc, isState);
    }

    public String parseResponse() {
        return this.value + " g/s";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 8) {
            this.value = "-1";
            return this.value;
        }
        this.value = roundValue(Float.valueOf(((float) Long.parseLong(res.substring(4, 8), 16)) / 100.0f)).toString();
        return this.value;
    }

    public String validateZeros(String stValue) {
        Integer len = Integer.valueOf(stValue.length());
        if (len.intValue() == 3) {
            return "0" + stValue;
        }
        if (len.intValue() == 2) {
            return "00" + stValue;
        }
        if (len.intValue() == 1) {
            return "000" + stValue;
        }
        return stValue;
    }

    public Integer transformValue() {
        return Integer.valueOf(Math.round(Float.valueOf(Float.valueOf(Float.parseFloat(this.value)).floatValue() * 100.0f).floatValue()));
    }

    public Boolean validateInput(String val) {
        boolean z = false;
        if (TextUtils.isEmpty(val) || val.length() > 8) {
            return false;
        }
        try {
            if (Float.valueOf(Float.parseFloat(val)).floatValue() < 65536.0f) {
                z = true;
            }
            return Boolean.valueOf(z);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
