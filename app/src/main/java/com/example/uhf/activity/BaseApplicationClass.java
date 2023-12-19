package com.example.uhf.activity;

import static com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.uhf.api.Communicator;
import com.example.uhf.api.RegistrationAssets;
import com.example.uhf.api.SyncResponse;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.tools.NetworkMonitor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.appcenter.crashes.Crashes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class BaseApplicationClass extends Application {
    private boolean connection = false; // Default value is false
    private List<ItemLocation> registeredItems = new ArrayList<ItemLocation>();
    SharedPreferences sharedPreferences;
    private String url;
    private String token;
    Communicator communicator;
    private SyncResponse response;

    private int currentUser = 0;

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
        loadRegisteredItems();
    }
    public boolean isConnection() {
        return connection;
    }

    public void setConnection(boolean connection) {
        this.connection = connection;
    }

    public void addRegistrationItem(ItemLocation asset) {
        try {
            if(registeredItems!=null) {
                registeredItems.add(asset);
            } else {
                registeredItems = new ArrayList<ItemLocation>();
                registeredItems.add(asset);
            }
        } catch (Exception e) {
            Crashes.trackError(e);
        } finally {
             saveRegisteredItems();
        }
    }
    private void saveRegisteredItems() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(registeredItems);
        editor.putString("registeredItems", json);
        editor.apply();
    }
    private void loadRegisteredItems() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("registeredItems", "");
        Type type = new TypeToken<ArrayList<ItemLocation>>(){}.getType();
        this.registeredItems = gson.fromJson(json, type);
    }

    public void setCurrentUser(int currentUser) {
        this.currentUser = currentUser;
    }
    public List<ItemLocation> getRegisteredItems() {
        return registeredItems;
    }
    public void clearAllRegisteredItems() {
        registeredItems.clear();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(registeredItems);
        editor.putString("registeredItems", json);
        editor.apply();
    }
    public void synchronizeAssets() throws IOException {

        response = new SyncResponse();
        RegistrationAssets registration = new RegistrationAssets(token);
        registration.addAssets(this.registeredItems, this.currentUser);
        String toJson = registration.toJson();
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = communicator.syncRegistrations(url, toJson);
                } catch (final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Crashes.trackError(e);
                        }
                    });
                }
            }
        });
        backgroundThread.start();
        try {
            backgroundThread.join();
        } catch (InterruptedException e) {
            Crashes.trackError(e);
        } finally {
            if(response.isSuccess()) {
                Toast.makeText(this, "Sinhronizacija se je končala brez napak.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Prišlo je do napake, nekateri podatki registracije so izgubljeni.", Toast.LENGTH_SHORT).show();
            }
            clearAllRegisteredItems();
        }
    }

}
