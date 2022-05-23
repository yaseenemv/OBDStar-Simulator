package com.obdsim.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.obdsim.CommandsActivity;
import com.obdsim.MainActivity;
import com.obdsim.R;
import com.obdsim.persistence.DataBaseService;
import com.obdsim.persistence.entities.MockObdCommand;

public class ConfirmDialog {
    public static void showDialog(Context context, String title, String msg, LayoutInflater inflater) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title).setMessage(msg).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.create().show();
    }

    public static AlertDialog.Builder getDialog(Context context, String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        CommandsActivity commandsActivity = (CommandsActivity) context;
        alertDialogBuilder.setTitle(title).setMessage(msg).setCancelable(false);
        return alertDialogBuilder;
    }

    public static AlertDialog getWaitTimeDialog(final MainActivity mainActivity) {
        LayoutInflater inflater = mainActivity.getLayoutInflater();
        TableLayout view = (TableLayout) inflater.inflate(R.layout.update_wait, (ViewGroup) null);
        View title = inflater.inflate(R.layout.update_command_title, (ViewGroup) null);
        ((TextView) title.findViewById(R.id.dialog_title)).setText("Update Wait Time");
        final EditText waitTime = (EditText) view.findViewById(R.id.edit_wait);
        waitTime.setText(Integer.valueOf(mainActivity.getWaitTime()).toString());
        return new AlertDialog.Builder(mainActivity).setCustomTitle(title).setView(view).setCancelable(false).setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mainActivity.setWaitTime(Integer.parseInt(waitTime.getText().toString()));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).create();
    }

    public static AlertDialog getUpdateDialog(CommandsActivity commandsActivity, MockObdCommand cmd) {
        LayoutInflater inflater = commandsActivity.getLayoutInflater();
        final DataBaseService db = commandsActivity.getDataBaseService();
        TableLayout view = (TableLayout) inflater.inflate(R.layout.update_cmd_dialog, (ViewGroup) null);
        View title = inflater.inflate(R.layout.update_command_title, (ViewGroup) null);
        final EditText editCmd = (EditText) view.findViewById(R.id.edit_command);
        final EditText editResponse = (EditText) view.findViewById(R.id.edit_response);
        editCmd.setText(cmd.getCmd());
        editResponse.setText(cmd.getResponse());
        final MockObdCommand mockObdCommand = cmd;
        final CommandsActivity commandsActivity2 = commandsActivity;
        return new AlertDialog.Builder(commandsActivity).setCustomTitle(title).setView(view).setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mockObdCommand.setCmd(editCmd.getText().toString());
                mockObdCommand.setResponse(editResponse.getText().toString());
                db.updateCommand(mockObdCommand);
                commandsActivity2.refreshRecyclerViewAdapter();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).create();
    }

    public static AlertDialog getStateUpdateDialog(final CommandsActivity commandsActivity, final MockObdCommand cmd) {
        LayoutInflater inflater = commandsActivity.getLayoutInflater();
        final DataBaseService db = commandsActivity.getDataBaseService();
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.staupdate_cmd_dialog, (ViewGroup) null);
        View title = inflater.inflate(R.layout.update_command_title, (ViewGroup) null);
        final EditText editResponse = (EditText) view.findViewById(R.id.edit_response);
        ((TextView) view.findViewById(R.id.edit_command)).setText(cmd.getDescription());
        editResponse.setText(cmd.getValue());
        return new AlertDialog.Builder(commandsActivity).setCustomTitle(title).setView(view).setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!cmd.validateInput(editResponse.getText().toString()).booleanValue()) {
                    commandsActivity.showToast("Invalid Input!");
                    return;
                }
                cmd.setValue(editResponse.getText().toString());
                cmd.setResponse(cmd.generateResponse());
                cmd.setValue();
                db.updateCommand(cmd);
                commandsActivity.refreshRecyclerViewAdapter();
                commandsActivity.showToast("Update Succesful!");
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).create();
    }

    public static AlertDialog getInsertDialog(CommandsActivity commandsActivity) {
        LayoutInflater inflater = commandsActivity.getLayoutInflater();
        final DataBaseService db = commandsActivity.getDataBaseService();
        final MockObdCommand cmd = new MockObdCommand();
        TableLayout view = (TableLayout) inflater.inflate(R.layout.update_cmd_dialog, (ViewGroup) null);
        View title = inflater.inflate(R.layout.update_command_title, (ViewGroup) null);
        ((TextView) title.findViewById(R.id.dialog_title)).setText("Add Command");
        final EditText editCmd = (EditText) view.findViewById(R.id.edit_command);
        final EditText editResponse = (EditText) view.findViewById(R.id.edit_response);
        final CommandsActivity commandsActivity2 = commandsActivity;
        return new AlertDialog.Builder(commandsActivity).setCustomTitle(title).setView(view).setPositiveButton("Add Command", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cmd.setCmd(editCmd.getText().toString());
                cmd.setResponse(editResponse.getText().toString());
                db.insertCommand(cmd);
                commandsActivity2.refreshRecyclerViewAdapterOnInsert(cmd);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).create();
    }

    public static AlertDialog getDeleteDialog(final CommandsActivity commandsActivity, final MockObdCommand cmd) {
        LayoutInflater inflater = commandsActivity.getLayoutInflater();
        final DataBaseService db = commandsActivity.getDataBaseService();
        View title = inflater.inflate(R.layout.update_command_title, (ViewGroup) null);
        ((TextView) title.findViewById(R.id.dialog_title)).setText("Remove Command");
        return new AlertDialog.Builder(commandsActivity).setCustomTitle(title).setMessage("Are you sure?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.deleteCommand(cmd);
                commandsActivity.refreshRecyclerViewAdapterOnDelete(cmd);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).create();
    }
}
