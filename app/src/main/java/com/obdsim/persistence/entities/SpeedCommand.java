package com.obdsim.persistence.entities;

import android.text.TextUtils;

public class SpeedCommand extends MockObdCommand {
    public SpeedCommand(String cmd, String response, String desc, Boolean bool) {
        super(cmd, response, desc, bool);
    }

    public SpeedCommand(int _id, String cmd, String response, Boolean stateFlag, String desc) {
        super(Integer.valueOf(_id), cmd, response, stateFlag, desc);
    }

    public SpeedCommand() {
        super("010D", "41 0D A0>", "Velocidad del vehículo", (Boolean) true);
    }

    public String parseResponse() {
        return this.value + " km/h";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 6) {
            this.value = "-1";
            return this.value;
        }
        this.value = Long.valueOf(Long.parseLong(res.substring(4, 6), 16)).toString();
        return this.value;
    }

    public String validateZeros(String stValue) {
        if (Integer.valueOf(stValue.length()).intValue() == 1) {
            return "0" + stValue;
        }
        return stValue;
    }

    public Integer transformValue() {
        return Integer.valueOf(this.value);
    }

    public Boolean validateInput(String val) {
        boolean z = false;
        if (TextUtils.isEmpty(val)) {
            return false;
        }
        if (TextUtils.isDigitsOnly(val) && Integer.valueOf(val).intValue() < 256) {
            z = true;
        }
        return Boolean.valueOf(z);
    }
}
