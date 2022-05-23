package com.obdsim;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.obdsim.persistence.DataBaseService;
import com.obdsim.tasks.BluetoothTask;
import com.obdsim.tasks.NetworkTask;
import com.obdsim.utils.ConfirmDialog;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private static final UUID MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final String NAME_INSECURE = "BluetoothChatInsecure";
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final int REQUEST_ENABLE_BT = 1234;
    private static final int UPDATE_COMMAND = 0;
    private static BluetoothTask listeningThread;
    private static NetworkTask listeningThreadN;
    TextView bluetoothDeviceStatus;
    Switch bluetoothScanButton;
    TextView networkDeviceStatus;
    Switch networkScanButton;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                switch (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE)) {
                    case 12:
                        // MainActivity.this.bluetoothScanButton.setOn(false);
                        MainActivity.this.bluetoothScanButton.setChecked(false);
                        MainActivity.this.bluetoothDeviceStatus.setText("Disconnected");
                        return;
                    default:
                        return;
                }
            } else {
                int wifiState = intent.getIntExtra("wifi_state", -1);
                if ("android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction()) && 3 == wifiState) {
                    MainActivity.this.networkScanButton.setChecked(false);
                    MainActivity.this.networkDeviceStatus.setText("Disconnected");
                }
            }
        }
    };
    int waitTime = 0;
    private DataBaseService dataBaseService;
    private Button startButton;
    private List<String> states = new ArrayList();
    private ListView status;
    private Button stopButton;

    public static String getNameSecure() {
        return NAME_SECURE;
    }

    public static String getNameInsecure() {
        return NAME_INSECURE;
    }

    public static UUID getMyUuid() {
        return MY_UUID;
    }

    public static UUID getMyUuidSecure() {
        return MY_UUID_SECURE;
    }

    public static UUID getMyUuidInsecure() {
        return MY_UUID_INSECURE;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.status = (ListView) findViewById(R.id.status);
        this.startButton = (Button) findViewById(R.id.startButton);
        this.stopButton = (Button) findViewById(R.id.stopButton);
        this.bluetoothDeviceStatus = (TextView) findViewById(R.id.bluetoothDeviceStatus);
        this.networkDeviceStatus = (TextView) findViewById(R.id.networkDeviceStatus);
        this.bluetoothScanButton = (Switch) findViewById(R.id.bluetoothScanButton);
        this.networkScanButton = (Switch) findViewById(R.id.networkScanButton);
        this.stopButton.setEnabled(false);
        this.status.setAdapter(new ArrayAdapter(this, R.layout.list_item_main, this.states));
        this.dataBaseService = new DataBaseService(this);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.ACCESS_WIFI_STATE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        }

        this.bluetoothScanButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (MainActivity.this.networkScanButton.isChecked()) {
                    MainActivity.this.networkScanButton.setChecked(false);
                    MainActivity.this.networkDeviceStatus.setText("Disconnected");
                }
                if (!isChecked) {

                    MainActivity.this.stop((View) null);
                } else if (MainActivity.this.getBluetoothAdapter() == null || MainActivity.this.getBluetoothAdapter().isEnabled()) {

                    MainActivity.this.start((View) null);
                } else {
                    MainActivity.this.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
                    return;
                }
                if (isChecked) {
                    MainActivity.this.bluetoothDeviceStatus.setText("Connected");
                } else {
                    MainActivity.this.bluetoothDeviceStatus.setText("Disconnected");
                }
            }
        });
        this.networkScanButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (MainActivity.this.bluetoothScanButton.isChecked()) {
                    MainActivity.this.bluetoothScanButton.setChecked(false);
                    MainActivity.this.bluetoothDeviceStatus.setText("Disconnected");
                }
                if (!isChecked) {

                    MainActivity.this.stopNetwork();
                } else if (MainActivity.this.wifiState()) {

                    MainActivity.this.startNetwork();
                } else {
                    Toast.makeText(MainActivity.this, "Turn on WIFI", Toast.LENGTH_SHORT).show();
                    MainActivity.this.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
                }
                if (isChecked) {
                    MainActivity.this.networkDeviceStatus.setText("Connected");
                } else {
                    MainActivity.this.networkDeviceStatus.setText("Disconnected");
                }
            }
        });


        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(this.mReceiver, filter);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        setIp();
    }

    private void setIp() {
        TextView tvIp = (TextView) findViewById(R.id.ipTV);
        TextView mcTV = (TextView) findViewById(R.id.mcTV);
        String ip = null;
        String address = null;
        try {
            ip = Formatter.formatIpAddress(((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress());
            address = getMacAddr();
        } catch (Exception e) {
        }
        if (ip != null) {
            tvIp.setText("Your IP:  " + ip);
        }
        if (address != null) {
            mcTV.setText("Your MAC:  " + address);
        }
    }

    public String getMacAddr() {
        return Settings.Secure.getString(getContentResolver(), "bluetooth_address");
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /* access modifiers changed from: private */
    public boolean wifiState() {
        return ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Update Wait Time");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                 ConfirmDialog.getWaitTimeDialog(this).show();
                return false;
            default:
                return false;
        }
    }

    public void start(View v) {
        try {
            this.states = new ArrayList();
            this.status.setAdapter(new ArrayAdapter(this, R.layout.list_item_main, this.states));
            updateStatus(getString(R.string.connecting), 0);
            BluetoothAdapter ba = startBluetooth();
            if (ba != null) {
                this.startButton.setEnabled(false);
                this.stopButton.setEnabled(true);
                if (listeningThread != null && !listeningThread.isCancelled()) {
                    listeningThread.cancel(true);
                    listeningThread = null;
                }
                listeningThread = new BluetoothTask(ba, this);
                listeningThread.execute();
                this.bluetoothDeviceStatus.setText("Connected");
                return;
            }
            updateStatus(getString(R.string.no_bluetooth_adapter), 2);
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public void startNetwork() {
        try {
            this.states = new ArrayList();
            this.status.setAdapter(new ArrayAdapter(this, R.layout.list_item_main, this.states));
            updateStatus(getString(R.string.connecting), 0);
            if (listeningThreadN != null && !listeningThreadN.isCancelled()) {
                listeningThreadN.cancel(true);
                listeningThreadN = null;
            }
            listeningThreadN = null;
            try {
                listeningThreadN = new NetworkTask(this);
                if (listeningThreadN.isCancelled()) {
                    Log.i("", "");
                }
            } catch (Exception ex) {
                ex.toString();
            }
            listeningThreadN.execute();
            this.networkDeviceStatus.setText("Connected");
        } catch (Exception ex2) {
            ex2.toString();
        }
    }

    public void stop(View v) {
        BluetoothSocket socket;
        try {
            showToast(getString(R.string.stopped_listening));
            this.states.clear();
            this.status.setAdapter(new ArrayAdapter(this, R.layout.list_item, this.states));
            this.startButton.setEnabled(true);
            this.stopButton.setEnabled(false);
            if (listeningThread != null && !listeningThread.isCancelled() && (socket = listeningThread.getSocket()) != null) {
                try {
                    socket.getInputStream().close();
                    socket.getOutputStream().close();
                    socket.close();
                } catch (IOException e) {
                    updateStatus(e.getMessage(), 2);
                }
                listeningThread.cancel(true);
                listeningThread = null;
            }
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public void stopNetwork() {
        try {
            showToast(getString(R.string.stopped_listening));
            this.states.clear();
            this.status.setAdapter(new ArrayAdapter(this, R.layout.list_item, this.states));
            this.startButton.setEnabled(true);
            this.stopButton.setEnabled(false);
            if (listeningThreadN != null && !listeningThreadN.isCancelled()) {
                Socket socket = listeningThreadN.getSocket();
                if (socket == null) {
                    listeningThreadN.cancel(true);
                    return;
                }
                try {
                    socket.getInputStream().close();
                    socket.getOutputStream().close();
                    socket.close();
                } catch (IOException e) {
                    updateStatus(e.getMessage(), 2);
                }
                listeningThreadN.cancel(true);
            }
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public void showCommands(View v) {
        startActivity(new Intent(this, CommandsActivity.class));
    }

    public void showStateCommands(View v) {
        startActivity(new Intent(this, StateCommandsActivity.class));
    }

    /* access modifiers changed from: protected */
    public BluetoothAdapter startBluetooth() {
        BluetoothAdapter mBluetoothAdapter = getBluetoothAdapter();
        if (mBluetoothAdapter == null) {
            showToast(getString(R.string.no_bluetooth));
            updateStatus(getString(R.string.no_bluetooth), 2);
        } else if (!mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), REQUEST_ENABLE_BT);
        }
        return mBluetoothAdapter;
    }

    public Button getStartButton() {
        return this.startButton;
    }

    public Button getStopButton() {
        return this.stopButton;
    }

    public void updateStatus(String newState, Integer type) {
        if (type.intValue() == 0) {
            newState = "[INFO] " + newState;
        }
        if (type.intValue() == 1) {
            newState = "[WARN] " + newState;
        }
        if (type.intValue() == 2) {
            newState = "[ERROR] " + newState;
        }
        if (type.intValue() == 3) {
            newState = "[INFO][RECEIVED] " + newState;
        }
        if (type.intValue() == 4) {
            newState = "[INFO][SENT] " + newState;
        }
        this.states.add(newState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_main, this.states);
        this.status.setAdapter(adapter);
        this.status.setSelection(adapter.getCount());
    }

    public void enableBluetooth() {
        if (getBluetoothAdapter() != null && !getBluetoothAdapter().isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
    }

    /* access modifiers changed from: private */
    public BluetoothAdapter getBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        BluetoothAdapter ba = getBluetoothAdapter();
        if (ba != null && ba.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            ba.disable();
        }
        super.onDestroy();
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public DataBaseService getDataBaseService() {
        return this.dataBaseService;
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(int waitTime2) {
        this.waitTime = waitTime2;
        if (listeningThread != null) {
            listeningThread.setWaitTime(waitTime2);
        }
    }

    public void onBackPressed() {
        if (listeningThread != null && !listeningThread.isCancelled()) {
            listeningThread.cancel(true);
            listeningThread = null;
        }
        super.onBackPressed();
    }
}
