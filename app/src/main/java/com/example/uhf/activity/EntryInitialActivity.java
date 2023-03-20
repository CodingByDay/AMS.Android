package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;

import java.util.List;

public class EntryInitialActivity extends AppCompatActivity {
private CardView btRegistration;
private CardView btInventory;
private CardView btListing;
private CardView btSync;
private CardView btLogout;
    private SettingsViewModel settingsView;
    private List<Setting> settingsList;
    private Setting token;

    private void initSettings() {
        settingsView = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsView.getAllItems().observe(this, new Observer<List<Setting>>() {
            @Override
            public void onChanged(List<Setting> settings) {
                settingsList = settings;
                for (Setting setting : settings) {
                    if(setting.getKey().equals("token")) {
                        token = setting;
                    }
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_initial);
        getSupportActionBar().hide();

        initSettings();
        btRegistration = (CardView) findViewById(R.id.btRegistration);
        btInventory = (CardView) findViewById(R.id.btInventory);
        btListing = (CardView) findViewById(R.id.btListing);
        btSync = (CardView) findViewById(R.id.btSync);
        btLogout = (CardView) findViewById(R.id.btLogout);



        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                token.setValue("");
                settingsView.update(token);
                Intent myIntent = new Intent(getApplicationContext(), LoginActivityMain.class);
                startActivity(myIntent);
                finishAffinity();
            }
        });





        btRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(myIntent);
            }
        });
        btInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                startActivity(myIntent);
            }
        });
        btListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), ListingActivity.class);
                startActivity(myIntent);
            }
        });
        btSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), EntryActivity.class);
                startActivity(myIntent);
            }
        });
        Toast.makeText(this, "Uspešna prijava.", Toast.LENGTH_SHORT).show();
    }
}