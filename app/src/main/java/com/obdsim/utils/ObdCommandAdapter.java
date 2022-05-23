package com.obdsim.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.obdsim.CommandsActivity;
import com.obdsim.R;
import com.obdsim.persistence.entities.MockObdCommand;

import java.util.List;

public class ObdCommandAdapter extends RecyclerView.Adapter<ObdCommandAdapter.MyViewHolder> {
    protected List<MockObdCommand> cmds;
    protected Context mContext;

    public ObdCommandAdapter(List<MockObdCommand> items, Context context) {
        this.cmds = items;
        this.mContext = context;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.list_item, (ViewGroup) null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MockObdCommand cmd = this.cmds.get(position);
        holder.cmdName.setText(cmd.getCmd());
        holder.responseValue.setText(cmd.getResponse());
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ConfirmDialog.getUpdateDialog((CommandsActivity) ObdCommandAdapter.this.mContext, cmd).show();
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ConfirmDialog.getDeleteDialog((CommandsActivity) ObdCommandAdapter.this.mContext, cmd).show();
            }
        });

    }


    public int getItemCount() {
        return this.cmds.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView cmdName;
        protected Button deleteBtn;
        protected TextView responseValue;
        protected Button updateBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.cmdName = (TextView) itemView.findViewById(R.id.textview_cmd_value);
            this.responseValue = (TextView) itemView.findViewById(R.id.textview_response_value);
            this.updateBtn = (Button) itemView.findViewById(R.id.button_update);
            this.deleteBtn = (Button) itemView.findViewById(R.id.button_delete);
        }
    }
}
