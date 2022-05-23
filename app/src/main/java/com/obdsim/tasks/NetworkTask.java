package com.obdsim.tasks;

import android.os.AsyncTask;

import com.obdsim.MainActivity;
import com.obdsim.R;
import com.obdsim.persistence.DataBaseService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkTask extends AsyncTask<String, ArrayList<String>, String> {
    byte[] buffer = new byte[1024];
    int bytesReceived;
    DataBaseService dataBaseService;
    Socket mClientSocket;
    ServerSocket serverSocket;
    private final MainActivity main;
    private InputStream mmInStream = null;
    private OutputStream mmOutStream = null;
    private int waitTime = 0;

    public NetworkTask(MainActivity main2) {
        this.main = main2;
        try {
            this.serverSocket = new ServerSocket();
            this.serverSocket.setReuseAddress(true);
            InetSocketAddress tmp = new InetSocketAddress(35000);
            try {
                this.serverSocket.bind(tmp);
                InetSocketAddress inetSocketAddress = tmp;
            } catch (Exception e) {
                InetSocketAddress inetSocketAddress2 = tmp;
                execute().toString();
                this.waitTime = main2.getWaitTime();
                this.dataBaseService = main2.getDataBaseService();
            }
        } catch (Exception e2) {
            execute().toString();
            this.waitTime = main2.getWaitTime();
            this.dataBaseService = main2.getDataBaseService();
        }
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
                this.mClientSocket = this.serverSocket.accept();
                if (this.mClientSocket != null) {
                    publishProgress(getPublishList(this.main.getString(R.string.accepted), "0"));
                    this.mmInStream = this.mClientSocket.getInputStream();
                    this.mmOutStream = this.mClientSocket.getOutputStream();
                    while (true) {
                        if (isCancelled() && !this.mClientSocket.isConnected()) {
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
                            if (this.mClientSocket != null && this.mClientSocket.isConnected() && !this.mClientSocket.isClosed()) {
                                this.mClientSocket.getInputStream().close();
                                this.mClientSocket.getOutputStream().close();
                                this.mClientSocket.close();
                            }
                            this.mClientSocket = null;
                        }
                    }
                } else {
                    publishProgress(getPublishList(this.main.getString(R.string.null_socket_n), "2"));
                    break;
                }
            } catch (IOException e2) {
                try {
                    this.mClientSocket.close();
                    this.mClientSocket = null;
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
        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                this.main.showToast(e.getMessage());
            }
        }
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(int waitTime2) {
        this.waitTime = waitTime2;
    }

    public Socket getSocket() {
        return this.mClientSocket;
    }

    public void setSocket(Socket socket) {
        this.mClientSocket = socket;
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                this.main.showToast(e.getMessage());
            }
        }
    }
}
