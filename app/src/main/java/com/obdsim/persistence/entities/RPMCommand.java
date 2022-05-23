package com.obdsim.persistence.entities;

import android.text.TextUtils;

public class RPMCommand extends MockObdCommand {
    public RPMCommand(String cmd, String response, String desc, Boolean bool) {
        super(cmd, response, desc, bool);
    }

    public RPMCommand(int _id, String cmd, String response, Boolean stateFlag, String desc) {
        super(Integer.valueOf(_id), cmd, response, stateFlag, desc);
    }

    public RPMCommand() {
        super("010C", "41 0C 32 96>", "RPM del motor", (Boolean) true);
    }

    public String parseResponse() {
        return this.value + " RPM";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 8) {
            this.value = "-1";
            return this.value;
        }
        this.value = Long.valueOf(Long.parseLong(res.substring(4, 8), 16) / 4).toString();
        return this.value;
    }

    public Integer transformValue() {
        return Integer.valueOf(Integer.valueOf(this.value).intValue() * 4);
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

    public Boolean validateInput(String val) {
        boolean z = false;
        if (TextUtils.isEmpty(val)) {
            return false;
        }
        if (TextUtils.isDigitsOnly(val) && val.length() < 65536) {
            z = true;
        }
        return Boolean.valueOf(z);
    }
}
