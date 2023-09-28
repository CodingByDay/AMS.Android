package com.example.uhf.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.barcode.Barcode;
import com.example.uhf.barcode.BarcodeUtility;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

public class ListingActivity extends AppCompatActivity implements Barcode {



    Button registration;
    Button logout;
    private SettingsViewModel settingsView;
    private List<Setting> settingsList;
    private SearchView swListing;
    private Setting token;
    private EditText tbBarcodeScan;
    private BarcodeUtility barcodeUtility;
    private View btSearch;
    private RFIDWithUHFUART mReader;

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
    private void initUHF() {
        try {
            mReader = RFIDWithUHFUART.getInstance();
        } catch (Exception ex) {
            return;
        }
        if (mReader != null) {
            new InitTask().execute();
        }
    }

    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;
        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            mReader.free();
            return mReader.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mypDialog.cancel();
            if (!result) {
                Toast.makeText(ListingActivity.this, "init fail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mypDialog = new ProgressDialog(ListingActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.riko_toolbar);
        setContentView(R.layout.activity_listing);
        btSearch = findViewById(R.id.btSearch);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean doWhile = true;
                while (doWhile) {
                    UHFTAGInfo tag = mReader.inventorySingleTag();
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                    float floatValue = -1000;
                    try {
                        Number parsedNumber = decimalFormat.parse(tag.getRssi());
                        floatValue = parsedNumber.floatValue();
                        System.out.println("Parsed float value: " + floatValue);
                    } catch (ParseException ignored) {

                    }
                    if (floatValue > - 33) {


                        Intent myIntent = new Intent(getApplicationContext(), LocationActivity.class);


                        // Comment
                    }
                }
            }
        });
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
                                fixedAssetsFragment.adapterFinal.searchByField(currentColumnSearch, newText);
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
                                fixedAssetsFragment.adapterFinal.findIdent(charSequence.toString());
                            }
                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });
                        initializeFragment();
                        initializeButtonEvents();
                        initSettings();
                        initUHF();
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