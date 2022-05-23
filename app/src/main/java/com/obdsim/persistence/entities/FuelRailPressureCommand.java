package com.obdsim.persistence.entities;

public class FuelRailPressureCommand extends RuntimeCommand {
    public FuelRailPressureCommand() {
        super("0123", "41 23 00 9B>", "Presión del medidor del tren de combustible", true);
    }

    public String parseResponse() {
        return this.value + " kPa";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 8) {
            this.value = "-1";
            return this.value;
        }
        Long val = Long.valueOf(Long.parseLong(res.substring(4, 8), 16) * 10);
        this.lValue = val;
        this.value = val.toString();
        return this.value;
    }

    public Integer transformValue() {
        return Integer.valueOf(Integer.valueOf(this.value).intValue() / 10);
    }
}
