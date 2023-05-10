package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_F1:
            {
                btRegistration.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F2:
            {
                btInventory.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F3:
            {
                btListing.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F4:
            {
                btSync.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F5:
            {
                //your Action code
                return true;
            }
            case KeyEvent.KEYCODE_F6:
            {
                //your Action code
                return true;
            }
            case KeyEvent.KEYCODE_F7:
            {
                //your Action code
                return true;
            }
            case KeyEvent.KEYCODE_F8:
            {
                btLogout.performClick();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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

                ProgressDialog mypDialog = new ProgressDialog(EntryInitialActivity.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mypDialog.setMessage("Odjava...");
                mypDialog.setCanceledOnTouchOutside(false);
                mypDialog.show();
                token.setValue("");
                settingsView.update(token);
                Intent myIntent = new Intent(getApplicationContext(), LoginActivityMain.class);
                startActivity(myIntent);
                finishAffinity();
                mypDialog.cancel();
            }
        });





        btRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog mypDialog = new ProgressDialog(EntryInitialActivity.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mypDialog.setMessage("Registracija...");
                mypDialog.setCanceledOnTouchOutside(false);
                mypDialog.show();
                Intent myIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(myIntent);
                mypDialog.cancel();
            }
        });

        btInventory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ProgressDialog mypDialog = new ProgressDialog(EntryInitialActivity.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mypDialog.setMessage("Inventura...");
                mypDialog.setCanceledOnTouchOutside(false);
                mypDialog.show();
                Intent myIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                startActivity(myIntent);
                mypDialog.cancel();
            }
        });
        btListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog mypDialog = new ProgressDialog(EntryInitialActivity.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mypDialog.setMessage("Pregled...");
                mypDialog.setCanceledOnTouchOutside(false);
                mypDialog.show();
                Intent myIntent = new Intent(getApplicationContext(), ListingActivity.class);
                startActivity(myIntent);
                mypDialog.cancel();
            }
        });
        btSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog mypDialog = new ProgressDialog(EntryInitialActivity.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mypDialog.setMessage("Prijava...");
                mypDialog.setCanceledOnTouchOutside(false);
                mypDialog.show();
                Intent myIntent = new Intent(getApplicationContext(), EntryActivity.class);
                startActivity(myIntent);
                mypDialog.cancel();
            }
        });
    }
}