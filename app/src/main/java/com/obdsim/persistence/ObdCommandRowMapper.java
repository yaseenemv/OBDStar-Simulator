package com.obdsim.persistence;

import android.database.Cursor;

import com.obdsim.persistence.entities.MockObdCommand;
import com.obdsim.utils.CommandsContainer;

public class ObdCommandRowMapper {
    public static MockObdCommand getCommand(Cursor c, Boolean setValue) {
        boolean z;
        try {
            MockObdCommand cmd = (MockObdCommand) CommandsContainer.getInstance().getMap().get(c.getString(1)).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            z = c.getInt(3) == 1;
            Boolean stateFlag = Boolean.valueOf(z);
            cmd.set_id(Integer.valueOf(c.getInt(0)));
            cmd.setCmd(c.getString(1));
            cmd.setResponse(c.getString(2));
            cmd.setStateFlag(stateFlag);
            cmd.setDescription(c.getString(4));
            if (setValue != null && setValue.booleanValue()) {
                cmd.setValue();
            }
            return cmd;
        } catch (Exception e) {
            return null;
        }
    }
}
