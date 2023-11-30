package com.example.uhf.activity;

import android.app.Application;

import com.example.uhf.tools.NetworkMonitor;


public class BaseApplicationClass extends Application {
    private boolean connection = false; // Default value is false

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkMonitor networkMonitor = NetworkMonitor.getInstance(getApplicationContext());
        networkMonitor.startNetworkMonitoring();
        // Perform initialization tasks or setup here
    }
    public boolean isConnection() {
        return connection;
    }

    public void setConnection(boolean connection) {
        this.connection = connection;
    }



}
