package com.obdsim.persistence.entities;

public class ThrottlePositionCommand extends FuelLevelCommand {
    public ThrottlePositionCommand() {
        super("0111", "41 11 96>", "Posición del acelerador", true);
    }
}
