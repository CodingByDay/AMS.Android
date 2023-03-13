package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.api.AsyncCallBack;
import com.example.uhf.api.Communicator;
import com.example.uhf.api.Root;
import com.example.uhf.api.RootAsset;
import com.example.uhf.api.RootLocation;
import com.example.uhf.api.RootStatus;
import com.example.uhf.database.ImportExportData;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public class EntryActivity extends AppCompatActivity implements AsyncCallBack {
    private CardView btTransferLocations;
    private CardView btTransferItems;
    private CardView btListingLocations;
    private CardView btListingItems;
    private CardView btExportListing;
    private CardView btTransferListing;
    private ProgressDialog mypDialog;
    private Communicator client;
    private List<Setting> settingsList;
    private Root root;
    private RootLocation rootLocation;
    private RootAsset rootAsset;
    private SettingsViewModel settingsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        getSupportActionBar().hide();
        client = new Communicator();
        initSettings();
        initializeViews();
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
        client.retrieveItems(EntryActivity.this, settingsList);
        client.retrieveLocations(EntryActivity.this, settingsList);
        client.retrieveAssets(EntryActivity.this, settingsList);
    }
    private void initializeViews() {
        btTransferLocations = findViewById(R.id.transferLocations);
        btTransferItems = findViewById(R.id.transferItems);
        btListingLocations = findViewById(R.id.listingLocations);
        btListingItems = findViewById(R.id.listingItems);
        btExportListing = findViewById(R.id.exportListing);
        btTransferListing = findViewById(R.id.transferListing);
        btTransferLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SyncData();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }


               // Intent myIntent = new Intent(getApplicationContext(), InventoryActivity.class);
               // startActivity(myIntent);
            }
        });
        btTransferItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(),  UHFMainActivity.class);
                startActivity(myIntent);
            }
        });
        btListingLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btListingItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btExportListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btTransferListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void setResult(Boolean result) {
        
    }

    @Override
    public void setResultListTypeItem(List< Item > items) {
        int result = items.size();
    }

    @Override
    public void setResultRoot(Root root) {
        this.root = root;
        mypDialog.setProgress(16);
        // 33.33 %
    }

    @Override
    public void setResultRootLocation(RootLocation rootLocation) {
        this.rootLocation = rootLocation;
        mypDialog.setProgress(32);
        // 66.66 %
    }

    @Override
    public void setResultRootStatus(RootStatus status) {
        // TODO: Ask about this
    }

    @Override
    public void setResultRootAsset(RootAsset asset) {
        this.rootAsset = asset;
        mypDialog.setProgress(48);
        // 100.00%
        ImportExportData importExportData = new ImportExportData(this);
        importExportData.commitToLocalStorage(root,rootLocation,rootAsset);
    }
    @Override
    public void setProgressValue(int progress) {
        mypDialog.setProgress(progress);
        if(progress == 100) {
            mypDialog.hide();
            mypDialog.cancel();
            Toast.makeText(this, "Podatki sinhronizirani", Toast.LENGTH_SHORT).show();
        }
    }
}