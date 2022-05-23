package com.obdsim;

import android.os.Bundle;
import android.view.Menu;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.obdsim.persistence.DataBaseService;
import com.obdsim.utils.StateObdCommandAdapter;

public class StateCommandsActivity extends CommandsActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_commands);
        this.dataBaseService = new DataBaseService(this);
        this.commands = this.dataBaseService.getCommands("state_flag=?", new String[]{"1"}, true);
        this.recyclerView = (RecyclerView) findViewById(R.id.command_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(new StateObdCommandAdapter(this.commands, this));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
