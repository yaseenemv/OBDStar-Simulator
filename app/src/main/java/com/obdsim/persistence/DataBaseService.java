package com.obdsim.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.obdsim.persistence.entities.MockObdCommand;
import com.obdsim.utils.CommandsContainer;

import java.util.ArrayList;
import java.util.Iterator;

public class DataBaseService extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CarDiag.db";
    public static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db = getWritableDatabase();

    public DataBaseService(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase db2) {
        db2.execSQL(ObdCommandContract.SQL_CREATE_COMMAND_ENTRIES);
        this.db = db2;
        if (getCommands((String) null, (String[]) null, (Boolean) null).size() == 0) {
            Iterator<MockObdCommand> it = CommandsContainer.getInstance().getCommandList().iterator();
            while (it.hasNext()) {
                insertCommand(it.next());
            }
        }
    }

    public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {
        db2.execSQL(ObdCommandContract.SQL_DELETE_COMMAND_ENTRIES);
        onCreate(db2);
    }

    public ArrayList<MockObdCommand> getCommands(String whereColumns, String[] whereColumnsValues, Boolean setValue) {
        ArrayList<MockObdCommand> cmds = new ArrayList<>();
        Cursor c = this.db.query(ObdCommandContract.CommandEntry.TABLE_NAME, new String[]{"_id", ObdCommandContract.CommandEntry.CODE, ObdCommandContract.CommandEntry.RESPONSE, ObdCommandContract.CommandEntry.STATE_FLAG, ObdCommandContract.CommandEntry.DESCRIPTION}, whereColumns, whereColumnsValues, (String) null, (String) null, (String) null);
        if (c.moveToFirst()) {
            do {
                MockObdCommand cmd = ObdCommandRowMapper.getCommand(c, setValue);
                if (cmd == null) {
                    cmd = new MockObdCommand(Integer.valueOf(c.getInt(0)), c.getString(1), c.getString(2), c.getString(4));
                }
                cmds.add(cmd);
            } while (c.moveToNext());
        }
        c.close();
        return cmds;
    }

    public String getResponse(String code) {
        String code2 = code.replace(" ", "");
        String res = "";
        Cursor c = this.db.query(ObdCommandContract.CommandEntry.TABLE_NAME, new String[]{ObdCommandContract.CommandEntry.RESPONSE}, "name=?", new String[]{code2}, (String) null, (String) null, (String) null);
        if (c.moveToFirst()) {
            do {
                res = c.getString(0);
            } while (c.moveToNext());
        }
        if (res == null || res.isEmpty()) {
            res = "OK>";
        }
        c.close();
        return res;
    }

    public void insertCommand(MockObdCommand cmd) {
        ContentValues values = new ContentValues();
        values.put(ObdCommandContract.CommandEntry.CODE, cmd.getCmd());
        values.put(ObdCommandContract.CommandEntry.RESPONSE, cmd.getResponse());
        values.put(ObdCommandContract.CommandEntry.STATE_FLAG, cmd.getStateFlag());
        values.put(ObdCommandContract.CommandEntry.DESCRIPTION, cmd.getDescription());
        cmd.set_id(Integer.valueOf(Long.valueOf(this.db.insert(ObdCommandContract.CommandEntry.TABLE_NAME, (String) null, values)).intValue()));
    }

    public void deleteCommand(MockObdCommand cmd) {
        this.db.delete(ObdCommandContract.CommandEntry.TABLE_NAME, "_id=?", new String[]{cmd.get_id().toString()});
    }

    public void updateCommand(MockObdCommand cmd) {
        ContentValues cv = new ContentValues();
        cv.put(ObdCommandContract.CommandEntry.CODE, cmd.getCmd());
        cv.put(ObdCommandContract.CommandEntry.RESPONSE, cmd.getResponse());
        this.db.update(ObdCommandContract.CommandEntry.TABLE_NAME, cv, "_id=?", new String[]{cmd.get_id().toString()});
    }
}
