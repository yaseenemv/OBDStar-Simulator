package com.obdsim.persistence;

import android.provider.BaseColumns;

public final class ObdCommandContract {
    public static final String SQL_CREATE_COMMAND_ENTRIES = "CREATE TABLE obd_command (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,response TEXT,state_flag INTEGER,description TEXT )";
    public static final String SQL_DELETE_COMMAND_ENTRIES = "DROP TABLE IF EXISTS obd_command";

    private ObdCommandContract() {
    }

    public static class CommandEntry implements BaseColumns {
        public static final String CODE = "name";
        public static final String DESCRIPTION = "description";
        public static final String RESPONSE = "response";
        public static final String STATE_FLAG = "state_flag";
        public static final String TABLE_NAME = "obd_command";
    }
}
