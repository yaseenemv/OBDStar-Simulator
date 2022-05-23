package com.obdsim;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.obdsim.persistence.DataBaseService;
import com.obdsim.persistence.entities.MockObdCommand;
import com.obdsim.utils.ConfirmDialog;
import com.obdsim.utils.ObdCommandAdapter;

import java.util.List;

public class CommandsActivity extends AppCompatActivity {
    private static final int ADD_COMMAND = 0;
    protected List<MockObdCommand> commands;
    protected DataBaseService dataBaseService;
    protected RecyclerView recyclerView;
    private Menu menu;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_commands);
        this.dataBaseService = new DataBaseService(this);
        this.commands = this.dataBaseService.getCommands((String) null, (String[]) null, false);
        this.recyclerView = (RecyclerView) findViewById(R.id.command_list);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(new ObdCommandAdapter(this.commands, this));
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        this.menu = menu2;
        menu2.add(0, 0, 0, "Add Command");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                ConfirmDialog.getInsertDialog(this).show();
                return false;
            default:
                return false;
        }
    }

    public DataBaseService getDataBaseService() {
        return this.dataBaseService;
    }

    public void refreshRecyclerViewAdapter() {
        this.recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void refreshRecyclerViewAdapterOnInsert(MockObdCommand cmd) {
        this.commands.add(cmd);
        refreshRecyclerViewAdapter();
    }

    public void refreshRecyclerViewAdapterOnDelete(MockObdCommand cmd) {
        this.commands.remove(cmd);
        refreshRecyclerViewAdapter();
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void addCommand(View v) {
        ConfirmDialog.getInsertDialog(this).show();
    }

    public void removeCommand(View v) {
        ConfirmDialog.getInsertDialog(this).show();
    }

    public List<MockObdCommand> getCommands() {
        return this.commands;
    }

    public void setCommands(List<MockObdCommand> commands2) {
        this.commands = commands2;
    }
}
