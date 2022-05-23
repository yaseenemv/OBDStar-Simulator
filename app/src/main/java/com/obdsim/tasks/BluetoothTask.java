package com.obdsim.tasks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import com.obdsim.MainActivity;
import com.obdsim.R;
import com.obdsim.persistence.DataBaseService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class BluetoothTask extends AsyncTask<String, ArrayList<String>, String> {
    private final BluetoothServerSocket mmServerSocket;
    byte[] buffer = new byte[1024];
    int bytesReceived;
    DataBaseService dataBaseService;
    BluetoothSocket socket;
    private final MainActivity main;
    private InputStream mmInStream = null;
    private OutputStream mmOutStream = null;
    private int waitTime = 0;

    public BluetoothTask(BluetoothAdapter mBluetoothAdapter, MainActivity main2) {
        BluetoothServerSocket tmp = null;
        this.main = main2;
        try {
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(MainActivity.getNameSecure(), MainActivity.getMyUuid());
        } catch (IOException e) {
        }
        this.mmServerSocket = tmp;
        this.waitTime = main2.getWaitTime();
        this.dataBaseService = main2.getDataBaseService();
    }

    /* access modifiers changed from: protected */
    public String doInBackground(String... strings) {
        while (true) {
            try {
                if (isCancelled()) {
                    break;
                }
                this.socket = this.mmServerSocket.accept();

                if (this.socket != null) {
                    publishProgress(getPublishList(this.main.getString(R.string.accepted), "0"));
                    this.mmInStream = this.socket.getInputStream();
                    this.mmOutStream = this.socket.getOutputStream();
                    while (true) {
                        if (isCancelled() && !this.socket.isConnected()) {
                            break;
                        }
                        try {
                            publishProgress(getPublishList(this.main.getString(R.string.receiving), "0"));
                            this.bytesReceived = this.mmInStream.read(this.buffer);
                            String readMessage = new String(this.buffer, 0, this.bytesReceived).trim();
                            publishProgress(getPublishList(readMessage, "3"));
                            if (this.waitTime > 0) {
                                Thread.sleep((long) this.waitTime);
                            }
                            String response = this.dataBaseService.getResponse(readMessage);
                            this.mmOutStream.write(response.getBytes());
                            publishProgress(getPublishList(response, "4"));
                        } catch (Exception e) {
                            publishProgress(getPublishList(this.main.getString(R.string.connection_closed), "0"));
                            publishProgress(getPublishList(this.main.getString(R.string.connecting), "0"));
                            this.socket.getInputStream().close();
                            this.socket.getOutputStream().close();
                            this.socket.close();
                            this.socket = null;
                            break;


                        }

                    }

                } else {
                    publishProgress(getPublishList(this.main.getString(R.string.null_socket), "2"));
                    break;
                }
            } catch (IOException e2) {
                try {
                    this.socket.close();
                    this.socket = null;
                } catch (IOException e1) {
                    publishProgress(getPublishList(this.main.getString(R.string.problem_closing_socket), "2"));
                    e1.printStackTrace();
                }
                publishProgress(getPublishList(this.main.getString(R.string.problem_receiving), "2"));
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(ArrayList<String>... progress) {
        ArrayList<String> parameters = progress[0];
        Integer code = Integer.valueOf(parameters.get(1));
        this.main.updateStatus(parameters.get(0), code);
    }

    public ArrayList<String> getPublishList(String message, String code) {
        ArrayList<String> paramList = new ArrayList<>();
        paramList.add(message);
        paramList.add(code);
        return paramList;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String result) {
        this.main.updateStatus(this.main.getString(R.string.process_terminated), 0);
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(int waitTime2) {
        this.waitTime = waitTime2;
    }

    public BluetoothSocket getSocket() {
        return this.socket;
    }

    public void setSocket(BluetoothSocket socket2) {
        this.socket = socket2;
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        if (this.mmServerSocket != null) {
            try {
                this.mmServerSocket.close();
            } catch (IOException e) {
                this.main.showToast(e.getMessage());
            }
        }
    }
}
