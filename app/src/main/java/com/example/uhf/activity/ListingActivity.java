package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.uhf.R;
import com.example.uhf.barcode.Barcode;
import com.example.uhf.barcode.BarcodeUtility;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;

import java.util.List;

public class ListingActivity extends AppCompatActivity implements Barcode {



    Button registration;
    Button logout;
    private SettingsViewModel settingsView;
    private List<Setting> settingsList;
    private SearchView swListing;
    private Setting token;
    private EditText tbBarcodeScan;
    private BarcodeUtility barcodeUtility;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_F1:
            {
                logout.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F2:
            {
                registration.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F3:
            {
                return true;
            }
            case KeyEvent.KEYCODE_F4:
            {
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
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_listing);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ListingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swListing = findViewById(R.id.swListing);
                        swListing.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                                String currentColumnSearch = fixedAssetsFragment.currentSearchColumn;
                                if(currentColumnSearch.equals("")) {
                                    currentColumnSearch = fixedAssetsFragment.first.getText().toString();
                                }
                                fixedAssetsFragment.adapter.searchByField(currentColumnSearch, newText);
                                return false;
                            }
                        });
                        tbBarcodeScan = findViewById(R.id.tbBarcodeScan);
                        barcodeUtility = new BarcodeUtility(ListingActivity.this, ListingActivity.this);
                        tbBarcodeScan.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                                fixedAssetsFragment.adapter.findIdent(charSequence.toString());
                            }
                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        initializeFragment();
                        initializeButtonEvents();
                        initSettings();
                    }
                });
            }
        }).start();

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
                Intent myIntent = new Intent(getApplicationContext(), EntryInitialActivity.class);
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

    @Override
    public void getResult(String result) {
        if(tbBarcodeScan.hasFocus()) {
            tbBarcodeScan.setText(result);
        }
    }
}