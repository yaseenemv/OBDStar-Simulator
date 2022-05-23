package com.obdsim.persistence.entities;

public class FuelPressureCommand extends SpeedCommand {
    public FuelPressureCommand() {
        super("010A", "41 0A 97>", "Presión del combustible", true);
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 6) {
            this.value = "-1";
            return this.value;
        }
        this.value = Long.valueOf(Long.parseLong(res.substring(4, 6), 16) * 3).toString();
        return this.value;
    }

    public String parseResponse() {
        return this.value + " kPa";
    }

    public Integer transformValue() {
        return Integer.valueOf(Integer.valueOf(this.value).intValue() / 3);
    }
}
