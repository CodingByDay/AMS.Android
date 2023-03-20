package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationCache;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.Model.LocationDAO;
import com.example.uhf.mvvm.ViewModel.ItemLocationCacheViewModel;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;
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
    private CardView btLogout;
    private ProgressDialog mypDialog;
    private Communicator client;
    private List<Setting> settingsList;
    private Root root;
    private RootLocation rootLocation;
    private RootAsset rootAsset;
    private SettingsViewModel settingsView;
    private boolean locationsClass;
    private ItemLocationViewModel itemLocationViewModel;
    private ItemLocationCacheViewModel itemLocationCacheViewModel;
    private List<ItemLocationCache> itemsLocationsCacheClassLevel;

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
    private void SyncData(boolean locations) throws JsonProcessingException {

        if(locations) {
            locationsClass = true;
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
            client.retrieveLocations(EntryActivity.this, settingsList);
        } else {
            locationsClass = false;
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
            client.retrieveAssets(EntryActivity.this, settingsList);
        }

    }





    private void initializeViews() {
        btTransferLocations = findViewById(R.id.transferLocations);
        btTransferItems = findViewById(R.id.transferItems);
        btListingLocations = findViewById(R.id.listingLocations);
        btListingItems = findViewById(R.id.listingItems);
        btExportListing = findViewById(R.id.exportListing);
        btTransferListing = findViewById(R.id.transferListing);
        btLogout = findViewById(R.id.btLogout);


        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        btTransferLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SyncData(true);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btTransferItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SyncData(false);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
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
                initSynchronization();
            }
        });
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
        mypDialog.setProgress(50);
        // 33.33 %
    }

    @Override
    public void setResultRootLocation(RootLocation rootLocation) {
        this.rootLocation = rootLocation;
        mypDialog.setProgress(1);
        ImportExportData importExportData = new ImportExportData(this);
        importExportData.commitToLocalStorageLocations(rootLocation);
    }







    @Override
    public void setResultRootStatus(RootStatus status) {
        // TODO: Ask about this
    }

    @Override
    public void setResultRootAsset(RootAsset asset) {
        this.rootAsset = asset;
        mypDialog.setProgress(99);
        mypDialog.setMessage("Zapisovanje podatkov.");
        // 100.00%
        ImportExportData importExportData = new ImportExportData(this);
        importExportData.commitToLocalStorageMaterial(root,rootAsset);
    }
    @Override
    public void setProgressValue(int progress) {
        mypDialog.setProgress(progress);
        if(progress >= 100) {
            mypDialog.hide();
            mypDialog.cancel();
            Toast.makeText(this, "Podatki sinhronizirani", Toast.LENGTH_SHORT).show();
        }
    }
}