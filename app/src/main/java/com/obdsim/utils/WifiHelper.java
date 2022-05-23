package com.obdsim.utils;

public class WifiHelper {
    private static boolean isConnectedToWifi;
    private static WifiConnectionChange sListener;

    public static void setWifiConnected(boolean connected) {
        isConnectedToWifi = connected;
        if (sListener != null) {
            sListener.wifiConnected(connected);
            sListener = null;
        }
    }

    public static void setWifiListener(WifiConnectionChange listener) {
        sListener = listener;
    }

    public interface WifiConnectionChange {
        void wifiConnected(boolean z);
    }
}
