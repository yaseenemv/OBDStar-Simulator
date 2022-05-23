package com.obdsim.persistence.entities;

public class WidebandAirFuelRatioCommand extends MassAirFlowCommand {
    public WidebandAirFuelRatioCommand() {
        super("0134", "41 34 34 87>", "Sensor de oxígeno 1", true);
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

    public Float roundValue(Float val) {
        return Float.valueOf(((float) Long.valueOf(Float.valueOf((val.floatValue() * 1000.0f) + 0.5f).longValue()).longValue()) / 1000.0f);
    }

    public Integer transformValue() {
        return Integer.valueOf(Math.round(Float.valueOf((Float.valueOf(Float.parseFloat(this.value)).floatValue() * 32768.0f) / 14.7f).floatValue()));
    }
}
