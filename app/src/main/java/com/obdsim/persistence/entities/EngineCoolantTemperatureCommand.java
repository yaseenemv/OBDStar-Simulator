package com.obdsim.persistence.entities;

import android.text.TextUtils;

public class EngineCoolantTemperatureCommand extends MockObdCommand {
    public EngineCoolantTemperatureCommand(String cmd, String response, String desc, Boolean bool) {
        super(cmd, response, desc, bool);
    }

    public EngineCoolantTemperatureCommand(int _id, String cmd, String response, Boolean stateFlag, String desc) {
        super(Integer.valueOf(_id), cmd, response, stateFlag, desc);
    }

    public EngineCoolantTemperatureCommand() {
        super("0105", "41 05 A1>", "Temperatura del líquido refrigerante del motor", (Boolean) true);
    }

    public String parseResponse() {
        return this.value + " °C";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 6) {
            this.value = "-1";
            return this.value;
        }
        this.value = Long.valueOf(Long.parseLong(res.substring(4, 6), 16) - 40).toString();
        return this.value;
    }

    public String validateZeros(String stValue) {
        if (Integer.valueOf(stValue.length()).intValue() == 1) {
            return "0" + stValue;
        }
        return stValue;
    }

    public Integer transformValue() {
        return Integer.valueOf(Integer.valueOf(this.value).intValue() + 40);
    }

    public Boolean validateInput(String val) {
        boolean z = false;
        if (TextUtils.isEmpty(val)) {
            return false;
        }
        Integer intVal = Integer.valueOf(val);
        if (TextUtils.isDigitsOnly(val) && intVal.intValue() < 256) {
            z = true;
        }
        return Boolean.valueOf(z);
    }
}
