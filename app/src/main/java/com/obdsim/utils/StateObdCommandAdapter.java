package com.obdsim.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obdsim.CommandsActivity;
import com.obdsim.R;
import com.obdsim.StateCommandsActivity;
import com.obdsim.persistence.entities.MockObdCommand;

import java.util.List;

public class StateObdCommandAdapter extends ObdCommandAdapter {
    public StateObdCommandAdapter(List<MockObdCommand> items, Context context) {
        super(items, context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.state_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        StateCommandsActivity stateCommandsActivity = (StateCommandsActivity) this.mContext;
        final MockObdCommand cmd = (MockObdCommand) this.cmds.get(position);
        holder.cmdName.setText(cmd.getDescription());
        holder.responseValue.setText(cmd.parseResponse());
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ConfirmDialog.getStateUpdateDialog((CommandsActivity) StateObdCommandAdapter.this.mContext, cmd).show();
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ConfirmDialog.getDeleteDialog((CommandsActivity) StateObdCommandAdapter.this.mContext, cmd).show();
            }
        });
    }
}
