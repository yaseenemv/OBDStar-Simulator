package com.obdsim.persistence.entities;

public class ModuleVoltageCommand extends MassAirFlowCommand {
    public ModuleVoltageCommand() {
        super("0142", "41 42 0F 0F>", "Voltaje del módulo de control", true);
    }

    public String parseResponse() {
        return this.value + " V";
    }

    public String generateResponse() {
        String responseHeader = prepareCommandResponse();
        return responseHeader + putSpaces(validateZeros(Integer.toHexString(transformValue().intValue()))).toUpperCase() + ">";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 8) {
            this.value = "-1";
            return this.value;
        }
        this.value = roundValue(Float.valueOf(((float) Long.parseLong(res.substring(4, 8), 16)) / 1000.0f)).toString();
        return this.value;
    }

    public Integer transformValue() {
        return Integer.valueOf(Math.round(Float.valueOf(Float.valueOf(Float.parseFloat(this.value)).floatValue() * 1000.0f).floatValue()));
    }
}
