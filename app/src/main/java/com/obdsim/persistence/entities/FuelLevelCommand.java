package com.obdsim.persistence.entities;

import android.text.TextUtils;

public class FuelLevelCommand extends MockObdCommand {
    public FuelLevelCommand(String cmd, String response, String desc, Boolean bool) {
        super(cmd, response, desc, bool);
    }

    public FuelLevelCommand(int _id, String cmd, String response, Boolean stateFlag, String desc) {
        super(Integer.valueOf(_id), cmd, response, stateFlag, desc);
    }

    public FuelLevelCommand() {
        super("012F", "41 2F 64>", "Nivel de combustible", (Boolean) true);
    }

    public String parseResponse() {
        return this.value + " %";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 6) {
            this.value = "-1";
            return this.value;
        }
        this.value = roundValue(Float.valueOf(((float) Long.parseLong(res.substring(4, 6), 16)) / 2.55f)).toString();
        return this.value;
    }

    public String validateZeros(String stValue) {
        if (Integer.valueOf(stValue.length()).intValue() == 1) {
            return "0" + stValue;
        }
        return stValue;
    }

    public Integer transformValue() {
        return Integer.valueOf(Math.round(Float.valueOf(Float.valueOf(Float.parseFloat(this.value)).floatValue() * 2.55f).floatValue()));
    }

    public Boolean validateInput(String val) {
        boolean z = false;
        if (TextUtils.isEmpty(val) || val.length() > 4) {
            return false;
        }
        try {
            if (Float.valueOf(Float.parseFloat(val)).floatValue() < 256.0f) {
                z = true;
            }
            return Boolean.valueOf(z);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Float roundValue(Float val) {
        return Float.valueOf(((float) Long.valueOf(Float.valueOf((val.floatValue() * 10.0f) + 0.5f).longValue()).longValue()) / 10.0f);
    }
}
