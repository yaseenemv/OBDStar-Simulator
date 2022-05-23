package com.obdsim.utils;

import com.obdsim.persistence.entities.AbsoluteLoadCommand;
import com.obdsim.persistence.entities.AirFuelRatioCommand;
import com.obdsim.persistence.entities.AirIntakeTemperatureCommand;
import com.obdsim.persistence.entities.AmbientAirTemperatureCommand;
import com.obdsim.persistence.entities.BarometricPressureCommand;
import com.obdsim.persistence.entities.ConsumptionRateCommand;
import com.obdsim.persistence.entities.DistanceMILOnCommand;
import com.obdsim.persistence.entities.EngineCoolantTemperatureCommand;
import com.obdsim.persistence.entities.FuelLevelCommand;
import com.obdsim.persistence.entities.FuelPressureCommand;
import com.obdsim.persistence.entities.FuelRailPressureCommand;
import com.obdsim.persistence.entities.FuelTrimLTB1;
import com.obdsim.persistence.entities.FuelTrimLTB2;
import com.obdsim.persistence.entities.FuelTrimSTB1;
import com.obdsim.persistence.entities.FuelTrimSTB2;
import com.obdsim.persistence.entities.IntakeManifoldPressureCommand;
import com.obdsim.persistence.entities.LoadCommand;
import com.obdsim.persistence.entities.MassAirFlowCommand;
import com.obdsim.persistence.entities.MockObdCommand;
import com.obdsim.persistence.entities.ModuleVoltageCommand;
import com.obdsim.persistence.entities.OilTempCommand;
import com.obdsim.persistence.entities.RPMCommand;
import com.obdsim.persistence.entities.RuntimeCommand;
import com.obdsim.persistence.entities.SpeedCommand;
import com.obdsim.persistence.entities.ThrottlePositionCommand;
import com.obdsim.persistence.entities.TimingAdvanceCommand;
import com.obdsim.persistence.entities.WidebandAirFuelRatioCommand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class CommandsContainer {
    private static CommandsContainer instance = null;
    ArrayList<MockObdCommand> commandList = new ArrayList<>();
    private final LinkedHashMap<String, Class<? extends MockObdCommand>> map = new LinkedHashMap<>();

    private CommandsContainer() {
        setCommands();
        setMapper();
    }

    public static CommandsContainer getInstance() {
        if (instance == null) {
            instance = new CommandsContainer();
        }
        return instance;
    }

    private void setCommands() {
        this.commandList.add(new MockObdCommand("ATZ", "ELM327 v1.3a\nOK>"));
        this.commandList.add(new MockObdCommand("ATE0", "OK>"));
        this.commandList.add(new MockObdCommand("ATE1", "OK>"));
        this.commandList.add(new MockObdCommand("ATL0", "OK>"));
        this.commandList.add(new MockObdCommand("ATM0", "OK>"));
        this.commandList.add(new MockObdCommand("ATS0", "OK>"));
        this.commandList.add(new MockObdCommand("ATST3e", "OK>"));
        this.commandList.add(new MockObdCommand("ATSP0", "OK>"));
        this.commandList.add(new MockObdCommand("ATSP1", "OK>"));
        this.commandList.add(new MockObdCommand("ATSP2", "OK>"));
        this.commandList.add(new MockObdCommand("ATSP3", "OK>"));
        this.commandList.add(new MockObdCommand("ATSP4", "OK>"));
        this.commandList.add(new MockObdCommand("ATSP5", "OK>"));
        this.commandList.add(new MockObdCommand("ATSP6", "OK>"));
        this.commandList.add(new MockObdCommand("ATSP7", "OK>"));
        this.commandList.add(new MockObdCommand("ATSP8", "OK>"));
        this.commandList.add(new MockObdCommand("ATSP9", "OK>"));
        this.commandList.add(new MockObdCommand("AT@1", "OBDII to RS232 Interpreter\nOK>"));
        this.commandList.add(new MockObdCommand("ATI", "ELM327 v1.3a\nOK>"));
        this.commandList.add(new MockObdCommand("ATH0", "OK>"));
        this.commandList.add(new MockObdCommand("ATH1", "OK>"));
        this.commandList.add(new MockObdCommand("ATAT1", "OK>"));
        this.commandList.add(new MockObdCommand("ATDPN", "A6>"));
        this.commandList.add(new MockObdCommand("0100", "41 00 FF FF FF FF>"));
        this.commandList.add(new MockObdCommand("0120", "41 20 FF FF FF FF>"));
        this.commandList.add(new MockObdCommand("0140", "41 40 FF FF FF FF>"));
        this.commandList.add(new MockObdCommand("0160", "41 60 FF FF FF FF>"));
        this.commandList.add(new MockObdCommand("0180", "41 80 FF FF FF F0>"));
        this.commandList.add(new AmbientAirTemperatureCommand());
        this.commandList.add(new ModuleVoltageCommand());
        this.commandList.add(new AbsoluteLoadCommand());
        this.commandList.add(new AirFuelRatioCommand());
        this.commandList.add(new MockObdCommand("0121", "41 21 10 00>"));
        this.commandList.add(new DistanceMILOnCommand());
        this.commandList.add(new TimingAdvanceCommand());
        this.commandList.add(new MockObdCommand("03", "43 05 7F 01 26 D3 97>"));
        this.commandList.add(new MockObdCommand("0902", "31 48 47 42 48 34 31 4A 58 4D 4E 31 30 39 31 38 36>"));
        this.commandList.add(new LoadCommand());
        this.commandList.add(new RPMCommand());
        this.commandList.add(new RuntimeCommand());
        this.commandList.add(new MassAirFlowCommand());
        this.commandList.add(new ThrottlePositionCommand());
        this.commandList.add(new MockObdCommand("0151", "41 51 01>"));
        this.commandList.add(new ConsumptionRateCommand());
        this.commandList.add(new FuelLevelCommand());
        this.commandList.add(new FuelTrimSTB1());
        this.commandList.add(new FuelTrimLTB1());
        this.commandList.add(new FuelTrimSTB2());
        this.commandList.add(new FuelTrimLTB2());
        this.commandList.add(new WidebandAirFuelRatioCommand());
        this.commandList.add(new OilTempCommand());
        this.commandList.add(new BarometricPressureCommand());
        this.commandList.add(new FuelPressureCommand());
        this.commandList.add(new FuelRailPressureCommand());
        this.commandList.add(new IntakeManifoldPressureCommand());
        this.commandList.add(new AirIntakeTemperatureCommand());
        this.commandList.add(new EngineCoolantTemperatureCommand());
        this.commandList.add(new SpeedCommand());
    }

    public void setMapper() {
        Iterator<MockObdCommand> it = this.commandList.iterator();
        while (it.hasNext()) {
            MockObdCommand cmd = it.next();
            if (cmd.getStateFlag().booleanValue()) {
                this.map.put(cmd.getCmd(), cmd.getClass());
            }
        }
    }

    public LinkedHashMap<String, Class<? extends MockObdCommand>> getMap() {
        return this.map;
    }

    public ArrayList<MockObdCommand> getCommandList() {
        return this.commandList;
    }
}
