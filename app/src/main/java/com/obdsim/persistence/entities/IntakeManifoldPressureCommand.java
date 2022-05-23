package com.obdsim.persistence.entities;

public class IntakeManifoldPressureCommand extends SpeedCommand {
    public IntakeManifoldPressureCommand() {
        super("010B", "41 0B 99>", "Presión absoluta del colector de admisión", true);
    }

    public String parseResponse() {
        return this.value + " kPa";
    }
}
