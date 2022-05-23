package com.obdsim.persistence.entities;

public class ConsumptionRateCommand extends MassAirFlowCommand {
    public ConsumptionRateCommand() {
        super("015E", "41 5E 00 78>", "Índice de consumo de combustible", true);
    }

    public String parseResponse() {
        return this.value + " L/h";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 8) {
            this.value = "-1";
            return this.value;
        }
        this.value = roundValue(Float.valueOf(((float) Long.parseLong(res.substring(4, 8), 16)) * 0.05f)).toString();
        return this.value;
    }

    public Integer transformValue() {
        return Integer.valueOf(Math.round(Float.valueOf(Float.valueOf(Float.parseFloat(this.value)).floatValue() / 0.05f).floatValue()));
    }
}
