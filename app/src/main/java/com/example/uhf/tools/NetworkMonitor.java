package com.example.uhf.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import com.example.uhf.activity.BaseApplicationClass;

public class NetworkMonitor {

    private static NetworkMonitor instance;
    private ConnectivityManager.NetworkCallback networkCallback;
    private Context context;
    private BaseApplicationClass applicationClass;

    private NetworkMonitor(Context context) {
        this.context = context.getApplicationContext();
        this.applicationClass = (BaseApplicationClass) context.getApplicationContext();
        startNetworkMonitoring();
    }

    public static synchronized NetworkMonitor getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkMonitor(context);
        }
        return instance;
    }

    public void startNetworkMonitoring() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    // Network is available

                    applicationClass.setConnection(true); // Update connection status
                }

                @Override
                public void onLost(Network network) {
                    // Network is lost

                    applicationClass.setConnection(false); // Update connection status
                }
            };

            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        }
    }

    public void stopNetworkMonitoring() {
        if (networkCallback != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.unregisterNetworkCallback(networkCallback);
            }
        }
    }
}
