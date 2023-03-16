package com.example.uhf.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.uhf.R;
import com.example.uhf.api.AsyncCallBack;
import com.example.uhf.api.Communicator;
import com.example.uhf.api.Root;
import com.example.uhf.api.RootAsset;
import com.example.uhf.api.RootLocation;
import com.example.uhf.api.RootStatus;
import com.example.uhf.database.ImportExportData;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationCache;
import com.example.uhf.mvvm.ViewModel.ItemLocationCacheViewModel;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;
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
    // Jackson properties
    private Root root;
    private RootLocation rootLocation;
    private RootAsset rootAsset;


    private ItemLocationViewModel itemLocationViewModel;
    private ItemLocationCacheViewModel itemLocationCacheViewModel;
    private List<ItemLocationCache> itemsLocationsCacheClassLevel;
    // Jackson properties

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        client = new Communicator();
        getSupportActionBar().show();
        initSettings();
        initPageViews();
        initSynchronization();
    }

    private void initSynchronization() {
        itemLocationViewModel = ViewModelProviders.of(this).get(ItemLocationViewModel.class);
        // if cached table is not empty and there is internet sync the data
        itemLocationCacheViewModel = ViewModelProviders.of(this).get(ItemLocationCacheViewModel.class);
        itemLocationCacheViewModel.getAllItems().observe(this, new Observer<List<ItemLocationCache>>() {
            @Override
            public void onChanged(List<ItemLocationCache> items) {
                 itemsLocationsCacheClassLevel = items;
                 if(itemsLocationsCacheClassLevel.size() > 0) {
                     syncDatabase(itemsLocationsCacheClassLevel);
                 }
            }
        });


        itemLocationViewModel = ViewModelProviders.of(this).get(ItemLocationViewModel.class);
        // if cached table is not empty and there is internet sync the data
        itemLocationViewModel = ViewModelProviders.of(this).get(ItemLocationViewModel.class);
        itemLocationViewModel.getAllItems().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> items) {
                int r = 9+9;
            }
        });

    }

    private void syncDatabase(List<ItemLocationCache> itemsLocationsCacheClassLevel) {
        for (ItemLocationCache itemLocationCache: itemsLocationsCacheClassLevel) {
            itemLocationViewModel.updateEPCByID(itemLocationCache.getID(), itemLocationCache.getEcd());
            itemLocationCacheViewModel.delete(itemLocationCache);

        }

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
                try {
                    SyncData();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
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
    private void SyncData() throws JsonProcessingException {
        boolean connected = client.isDeviceConnected(this);
        if(!connected) {
        Toast.makeText(this, "Ni povezave!", Toast.LENGTH_SHORT).show();
         return;
        }
        mypDialog = new ProgressDialog(this);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mypDialog.setMax(100);
        mypDialog.setMessage("Pridobivanje podatkov...");
        mypDialog.setCanceledOnTouchOutside(false);
        mypDialog.show();
        // Continue here
        client.retrieveItems(LoginActivityMain.this, settingsList);
        client.retrieveLocations(LoginActivityMain.this, settingsList);
        client.retrieveAssets(LoginActivityMain.this, settingsList);
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
                    boolean successs = false;
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
            Toast.makeText(this, "Napačni podatki.", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(getApplicationContext(), EntryInitialActivity.class);
            startActivity(myIntent);
        }
    }
    @Override
    public void setResultListTypeItem(List<Item> items) {
        int result = items.size();
    }

    @Override
    public void setResultRoot(Root root) {


    }

    @Override
    public void setResultRootLocation(RootLocation rootLocation) {


    }

    @Override
    public void setResultRootStatus(RootStatus status) {

    }

    @Override
    public void setResultRootAsset(RootAsset asset) {

    }
    @Override
    public void setProgressValue(int progress) {

    }
}