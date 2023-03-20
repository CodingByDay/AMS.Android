package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uhf.R;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;

import java.util.List;

public class ListingActivity extends AppCompatActivity {



    Button registration;
    Button logout;
    private SettingsViewModel settingsView;
    private List<Setting> settingsList;

    private Setting token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_listing);
        initializeFragment();
        initializeButtonEvents();
        initSettings();
    }

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


    private void initializeButtonEvents() {
        registration = findViewById(R.id.btRequest);
        logout = findViewById(R.id.btExit);


        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(myIntent);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), LoginActivityMain.class);
                startActivity(myIntent);
                finishAffinity();
            }
        });
    }

    private void initializeFragment() {
        replaceFragment(new FixedAssetsFragment());
    }
    // Replaces a fragment
    private void replaceFragment(Fragment fragment) {
        // Data for knowing who is the caller
        Bundle arguments = new Bundle();
        arguments.putString( "callerID" , "ListingActivity");
        fragment.setArguments(arguments);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}