package com.obdsim.persistence.entities;

public class TimingAdvanceCommand extends FuelLevelCommand {
    public TimingAdvanceCommand() {
        super("010E", "41 0E 84>", "Avance del tiempo", true);
    }
}
