package com.obdsim.persistence.entities;

import android.text.TextUtils;

public class AbsoluteLoadCommand extends MassAirFlowCommand {
    public AbsoluteLoadCommand() {
        super("0143", "41 43 00 3E>", "Valor absoluto de carga", true);
    }

    public String parseResponse() {
        return this.value + " %";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 8) {
            this.value = "-1";
            return this.value;
        }
        this.value = roundValue(Float.valueOf((((float) Long.parseLong(res.substring(4, 8), 16)) * 100.0f) / 255.0f)).toString();
        return this.value;
    }

    public Integer transformValue() {
        return Integer.valueOf(Math.round(Float.valueOf(Float.valueOf(Float.parseFloat(this.value)).floatValue() * 2.55f).floatValue()));
    }

    public Boolean validateInput(String val) {
        boolean z = false;
        if (TextUtils.isEmpty(val) || val.length() > 8) {
            return false;
        }
        try {
            if (Float.valueOf(Float.parseFloat(val)).floatValue() < 100.0f) {
                z = true;
            }
            return Boolean.valueOf(z);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
