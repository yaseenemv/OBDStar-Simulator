package com.obdsim.persistence.entities;

public class BarometricPressureCommand extends SpeedCommand {
    public BarometricPressureCommand() {
        super("0133", "41 33 96>", "Presión barométrica", true);
    }

    public String parseResponse() {
        return this.value + " kPa";
    }
}
