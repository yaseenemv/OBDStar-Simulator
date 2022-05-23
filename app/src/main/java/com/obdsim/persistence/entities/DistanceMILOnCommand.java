package com.obdsim.persistence.entities;

public class DistanceMILOnCommand extends RuntimeCommand {
    public DistanceMILOnCommand() {
        super("0121", "41 21 10 00>", "Distancia recorrida con la luz de falla", true);
    }

    public String parseResponse() {
        return this.value + " km";
    }
}
