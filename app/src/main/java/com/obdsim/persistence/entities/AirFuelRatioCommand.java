package com.obdsim.persistence.entities;

public class AirFuelRatioCommand extends MassAirFlowCommand {
    public AirFuelRatioCommand() {
        super("0144", "41 44 64 FF>", "Relación combustible - aire", true);
    }

    public String parseResponse() {
        return this.value + ":1 AFR";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 8) {
            this.value = "-1";
            return this.value;
        }
        this.value = roundValue(Float.valueOf((((float) Long.parseLong(res.substring(4, 8), 16)) / 32768.0f) * 14.7f)).toString();
        return this.value;
    }

    public Integer transformValue() {
        return Integer.valueOf(Math.round(Float.valueOf((Float.valueOf(Float.parseFloat(this.value)).floatValue() * 32768.0f) / 14.7f).floatValue()));
    }
}
