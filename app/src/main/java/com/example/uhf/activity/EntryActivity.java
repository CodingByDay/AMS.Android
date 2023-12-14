package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationCache;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.Model.LocationDAO;
import com.example.uhf.mvvm.ViewModel.CheckOutViewModel;
import com.example.uhf.mvvm.ViewModel.ItemLocationCacheViewModel;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsoft.appcenter.crashes.Crashes;

import java.io.IOException;
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
    private int counter = 0;
    private CheckOutViewModel checkOutViewModel;
    private List<CheckOut> checkOutItems;
    private ProgressDialog progressDialog;

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


        checkOutViewModel = ViewModelProviders.of(this).get(CheckOutViewModel.class);
        checkOutViewModel.getAllItems().observe(this, new Observer<List<CheckOut>>() {
            @Override
            public void onChanged(List<CheckOut> checkOuts) {
                checkOutItems = checkOuts;
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


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F1: {
                btTransferLocations.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F2: {
                btTransferItems.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F3: {
                btListingLocations.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F4: {
                btListingItems.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F5: {
                //your Action code
                return true;
            }
            case KeyEvent.KEYCODE_F6: {
                btExportListing.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F7: {
                btTransferListing.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F8: {

                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
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
                Intent myIntent = new Intent(getApplicationContext(), ListingLocationsActivity.class);
                startActivity(myIntent);
            }
        });
        btListingItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), ListingAssetsActivity.class);
                startActivity(myIntent);
            }
        });
        btExportListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseApplicationClass baseApp = (BaseApplicationClass) getApplication();
                boolean isConnected = baseApp.isConnection();
                if(isConnected) {
                    if (checkOutItems.size() > 0) {
                        progressDialog = new ProgressDialog(EntryActivity.this);
                        progressDialog.setMessage("Prosimo počakajte..."); // Set your message here
                        progressDialog.setIndeterminate(true); // Use an indeterminate style spinner
                        progressDialog.setCancelable(false); // Make it non-cancelable
                        progressDialog.show();

                    for (CheckOut co : checkOutItems) {
                        try {
                            client.checkOutCommit(EntryActivity.this, settingsList, co);
                            // Delete all data from commit table after the state has been updated.
                        } catch (JsonProcessingException e) {
                            continue;
                        }
                    }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EntryActivity.this);
                        builder.setTitle("Ni podatkov.");
                        builder.setMessage("Ni podatkov za sinhronizacijo.");
                        // Add a button to dismiss the dialog
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Close the dialog
                                dialogInterface.dismiss();
                            }
                        });
                        // Create and show the AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                } else {
                        // Means there is no internet notify the user and abort.
                        AlertDialog.Builder builder = new AlertDialog.Builder(EntryActivity.this);
                        builder.setTitle("Ni povezave.");
                        builder.setMessage("Žal internetna povezava ni na voljo.");
                        // Add a button to dismiss the dialog
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Close the dialog
                                dialogInterface.dismiss();
                            }
                        });
                        // Create and show the AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                }
            }
        });
        btTransferListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {mypDialog = new ProgressDialog(EntryActivity.this);
                BaseApplicationClass baseApp = (BaseApplicationClass) getApplication();
                boolean isConnected = baseApp.isConnection();
                if(isConnected) {
                    try {
                        baseApp.synchronizeAssets();
                    } catch (IOException e) {
                        Crashes.trackError(e);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EntryActivity.this);
                    builder.setTitle("Ni povezave.");
                    builder.setMessage("Žal internetna povezava ni na voljo.");
                    // Add a button to dismiss the dialog
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Close the dialog
                            dialogInterface.dismiss();
                        }
                    });
                    // Create and show the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private int correctTransfers = 0;
    @Override
    public void setResult(Boolean result) {
        if(result) {
            correctTransfers += 1;
        }
        counter += 1;
        if(counter == checkOutItems.size()) {
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Stanje posodobljeno");
            builder.setMessage("Število uspešnih prenosov: " + correctTransfers + "/" + checkOutItems.size());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); // Dismiss the dialog.
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            counter = 0;
            correctTransfers = 0;
            checkOutViewModel.deleteAll();
        }
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
        }
    }
}