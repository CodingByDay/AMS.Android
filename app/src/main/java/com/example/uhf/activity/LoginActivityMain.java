package com.example.uhf.activity;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.uhf.R;
import com.example.uhf.api.AsyncCallBack;
import com.example.uhf.api.Communicator;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;

public class LoginActivityMain extends AppCompatActivity implements AsyncCallBack {
private Button login;
    private ProgressDialog mypDialog;
    private EditText tbCompany;
    private EditText tbUname;
    private EditText tbPassword;
    private List<Setting> settingsList = new ArrayList<Setting>();
    private Communicator client;

    public String token = "";
    private SettingsViewModel settingsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        client = new Communicator();
        getSupportActionBar().show();
        initSettings();
        initPageViews();



    }

    private void initSettings() {
        settingsView = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsView.getAllItems().observe(this, new Observer<List<Setting>>() {
            @Override
            public void onChanged(List<Setting> settings) {
                settingsList = settings;
            }

        });
    }


    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            case R.id.UHF_ver:
                String versionName = "";
                try {
                    versionName = this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("O aplikaciji");
                builder.setMessage("Inventura verzija: " + versionName);
                builder.setPositiveButton("Zapri", ((dialogInterface, i) -> {

                }));
                builder.show();
                break;
            case R.id.sync:
                 SyncData();
                break;
            case R.id.settings:
                Toast.makeText(this, "Nastavitve", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                //myIntent.putExtra("fragment", "entry_menu");
                startActivity(myIntent);
                break;
            default:
                break;
        }
        return true;
    }

    private void SyncData() {
        boolean connected = client.isDeviceConnected(this);
        if(!connected) {
        Toast.makeText(this, "Ni povezave!", Toast.LENGTH_SHORT).show();
         return;
        }
        mypDialog = new ProgressDialog(this);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mypDialog.setMessage("Pridobivanje podatkov...");
        mypDialog.setCanceledOnTouchOutside(false);
        mypDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        supportInvalidateOptionsMenu();
        invalidateOptionsMenu();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initPageViews() {
        login = (Button) findViewById(R.id.login);
        tbCompany = (EditText) findViewById(R.id.tbCompany);
        tbUname = (EditText) findViewById(R.id.tbUname);
        tbPassword = (EditText) findViewById(R.id.tbPassword);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Data
                String company = tbCompany.getText().toString();
                String uname = tbUname.getText().toString();
                String password = tbPassword.getText().toString();
                if(company.isEmpty()||uname.isEmpty()||password.isEmpty()) {
                    return;
                }
                // Call the API
                boolean success = false;
                try {
                    client.login(LoginActivityMain.this, settingsList, company, uname, password);
                } catch (JsonProcessingException e) {
                    // TODO handle exceptions better
                }

            }
        });
    }

    @Override
    public void setResult(Boolean result) {
        if(result) {
            if(!token.equals("")) {
                settingsView.insert(new Setting("token", token));
            }
            Intent myIntent = new Intent(getApplicationContext(), EntryInitialActivity.class);
            startActivity(myIntent);
        } else {
        Toast.makeText(this, "Napačni podatki.", Toast.LENGTH_SHORT).show(); }
    }
}