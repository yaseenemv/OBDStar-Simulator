package com.obdsim.persistence.entities;

public class LoadCommand extends FuelLevelCommand {
    public LoadCommand() {
        super("0104", "41 04 78>", "Carga calculada del motor", true);
    }
}
