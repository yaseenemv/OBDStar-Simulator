package com.obdsim.persistence.entities;

public class OilTempCommand extends EngineCoolantTemperatureCommand {
    public OilTempCommand() {
        super("015C", "41 5C 64>", "Temperatura del aceite del motor", true);
    }
}
