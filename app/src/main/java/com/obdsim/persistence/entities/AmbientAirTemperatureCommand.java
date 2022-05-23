package com.obdsim.persistence.entities;

public class AmbientAirTemperatureCommand extends EngineCoolantTemperatureCommand {
    public AmbientAirTemperatureCommand() {
        super("0146", "41 46 50>", "Temperatura del aire del ambiente", true);
    }
}
