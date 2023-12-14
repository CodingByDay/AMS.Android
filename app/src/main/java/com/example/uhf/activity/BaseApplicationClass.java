package com.example.uhf.activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.uhf.api.Communicator;
import com.example.uhf.api.RegistrationAssets;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.tools.NetworkMonitor;
import com.microsoft.appcenter.crashes.Crashes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BaseApplicationClass extends Application {
    private boolean connection = false; // Default value is false
    private List<ItemLocation> registeredItems = new ArrayList<ItemLocation>();
    SharedPreferences sharedPreferences;
    private String url;
    private String token;
    Communicator communicator;
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkMonitor networkMonitor = NetworkMonitor.getInstance(getApplicationContext());
        networkMonitor.startNetworkMonitoring();
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        url = sharedPreferences.getString("url", "");
        token = sharedPreferences.getString("token", "");
        communicator = new Communicator();
        // Perform initialization tasks or setup here
    }
    public boolean isConnection() {
        return connection;
    }

    public void setConnection(boolean connection) {
        this.connection = connection;
    }

    public void addRegistrationItem(ItemLocation asset) {
        try {
            registeredItems.add(asset);
        } catch (Exception e) {
            Crashes.trackError(e);
        }
    }

    public List<ItemLocation> getRegisteredItems() {
        return registeredItems;
    }

    public void clearAllRegisteredItems() {
        registeredItems.clear();
    }

    public void synchronizeAssets() throws IOException {
        RegistrationAssets registration = new RegistrationAssets(token);
        registration.addAssets(this.registeredItems);
        String toJson = registration.toJson();
        communicator.syncRegistrations(url, toJson);
    }

}
