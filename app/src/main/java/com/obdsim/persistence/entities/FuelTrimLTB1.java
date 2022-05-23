package com.obdsim.persistence.entities;

public class FuelTrimLTB1 extends FuelLevelCommand {
    public FuelTrimLTB1() {
        super("0107", "41 07 B4>", "Ajuste de combustible a largo plazo—Banco 1", true);
    }

    public FuelTrimLTB1(String cmd, String response, String desc, Boolean isState) {
        super(cmd, response, desc, isState);
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 6) {
            this.value = "-1";
            return this.value;
        }
        this.value = roundValue(Float.valueOf((((float) (Long.parseLong(res.substring(4, 6), 16) - 128)) * 100.0f) / 128.0f)).toString();
        return this.value;
    }

    public Integer transformValue() {
        return Integer.valueOf(Math.round(Float.valueOf(((Float.valueOf(Float.parseFloat(this.value)).floatValue() * 128.0f) / 100.0f) + 128.0f).floatValue()));
    }
}
