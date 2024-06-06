package com.example.uhf.activity;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.example.uhf.R;
import com.example.uhf.api.Communicator;
import com.example.uhf.api.Duplicate;
import com.example.uhf.barcode.Barcode;
import com.example.uhf.barcode.BarcodeUtility;
import com.example.uhf.custom.CustomAutoCompleteTextView;
import com.example.uhf.custom.CustomAutocompleteAdapter;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.fragment.KeyDwonFragment;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.ViewModel.CheckOutViewModel;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.mvvm.ViewModel.LocationViewModel;


import com.rscja.deviceapi.RFIDWithUHFUART;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;

public class InventoryActivity extends FragmentActivity implements Barcode {
private ItemViewModel itemViewModel;
private Button btConfirm;
public KeyDwonFragment currentFragment=null;
public CustomAutoCompleteTextView tbLocation;

public String currentLocation;
private Button btToggleScanning;
public RFIDWithUHFUART mReader;
    private BarcodeUtility barcodeUtility;
    private CustomAutocompleteAdapter locationsAdapter;
    private LocationViewModel locationsViewModel;
    private Button btExit;
    public ItemTemporary current;
    private SearchView swListing;
    private String location;
    private CheckOutViewModel checkOutViewModel;
    private List<CheckOut> checkOutItems;
    private ItemLocationViewModel itemLocationViewModel;
    private String locationOfficial;
    private Duplicate duplicate;
    private List<ItemTemporary> scanned;
    private int scannedIndex = 0;
    private List<ItemLocation> realItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        new Thread(new Runnable() {
            @Override
            public void run() {
                InventoryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        barcodeUtility = new BarcodeUtility(InventoryActivity.this, InventoryActivity.this);
                        initViews();
                        initializeFragment();
                    }
                });
            }
        }).start();
    }
    @Override
    public void onPause() {
        super.onPause();
        try {
        barcodeUtility.unregister();
        } catch(Exception e) {
            Sentry.captureException(e);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F1: {
                btExit.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F2: {
                btConfirm.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F3: {

                return true;
            }
            case KeyEvent.KEYCODE_F4: {

                return true;
            }
            case KeyEvent.KEYCODE_F5: {
                //your Action code
                return true;
            }
            case KeyEvent.KEYCODE_F6: {
                //your Action code
                return true;
            }
            case KeyEvent.KEYCODE_F7: {
                //your Action code
                return true;
            }
            case KeyEvent.KEYCODE_F8: {

                return true;
            }
            case 294: {
                btToggleScanning.performClick();
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    private int helpCounter = 0;
    private void initViews() {
        if(getIntent().getExtras()!=null) {
            Bundle extras = getIntent().getExtras();
            if (extras.getString("location") != null) {
                location = extras.getString("location");
            }
        }
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

                if(fixedAssetsFragment.temporaryAdapter!=null) {
                    fixedAssetsFragment.temporaryAdapter.searchByField(currentColumnSearch, newText);
                }
                return false;
            }
        });
        btExit = findViewById(R.id.btExit);
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                fixedAssetsFragment.stopScanning();
                Intent myIntent = new Intent(getApplicationContext(), EntryInitialActivity.class);
                startActivity(myIntent);
                finishAffinity();

            }
        });

        checkOutViewModel = ViewModelProviders.of(this).get(CheckOutViewModel.class);
        checkOutViewModel.getAllItems().observe(this, new Observer<List<CheckOut>>() {
            @Override
            public void onChanged(List<CheckOut> checkOuts) {


                checkOutItems = checkOuts;
            }
        });
        itemLocationViewModel = ViewModelProviders.of(this).get(ItemLocationViewModel.class);
        itemLocationViewModel.getAllItems().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> checkOuts) {



            }
        });

        btConfirm = findViewById(R.id.btConfirm);
        tbLocation = findViewById(R.id.atbLocation);

        btToggleScanning = findViewById(R.id.btToggleScanning);

        List<String> locations = new ArrayList<String>();
        locationsAdapter = new CustomAutocompleteAdapter(getBaseContext(),locations, tbLocation);
        locationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        locationsViewModel = ViewModelProviders.of(InventoryActivity.this).get(LocationViewModel.class);
        tbLocation.setAdapter(locationsAdapter);
        locationsViewModel.getAllItems().observe(InventoryActivity.this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> items) {
                List<String> locations = new ArrayList<String>();
                // Think about improving the time complexity here
                for (Location location : items) {
                    locations.add(location.getLocation());
                }


                locationsAdapter = new CustomAutocompleteAdapter(getBaseContext(),locations, tbLocation);
                tbLocation.setAdapter(locationsAdapter);

            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String url = sharedPreferences.getString("url", "");
                String token = sharedPreferences.getString("token", "");
                String checking = sharedPreferences.getString("checking", "");
                Communicator communicator = new Communicator();
                if(tbLocation.getText().toString().equals("")) {
                    Toast.makeText(InventoryActivity.this, "Lokacija mora biti izbrana", Toast.LENGTH_SHORT).show();
                    return;
                }
                FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                fixedAssetsFragment.stopScanning();
                scanned = fixedAssetsFragment.temporaryAdapter.items;
                realItems = fixedAssetsFragment.itemsClassLevel;
                LocalDate localDate = LocalDate.now();
                BaseApplicationClass baseApp = (BaseApplicationClass) getApplication();
                boolean isConnected = baseApp.isConnection();
                if(!isConnected || checking.equals("0")) {
                    for (int i = 0; i < scanned.size(); i++) {
                        ItemLocation item = findItemByEpc(realItems, scanned.get(i).getEcd());
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        CheckOut checkOutItem = new CheckOut(-1, item.getQid(), item.getItem(), tbLocation.getText().toString(),
                                item.getCode(), item.getEcd(), item.getName(), "", localDate.toString(), 5, "", -1, timestamp.toString(), 5, timestamp.toString(), 5, "");
                        checkOutViewModel.insert(checkOutItem);
                        ItemLocation toUpdate = item;
                        toUpdate.setLocation(currentLocation);
                        itemLocationViewModel.update(toUpdate);
                    }
                    Intent myIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("location_inventory", tbLocation.getText().toString());
                    editor.apply();
                    startActivity(myIntent);
                } else if (isConnected && checking.equals("1")) {
                    checkLocationsServer();
                }
            }
        });
        btToggleScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btText = btToggleScanning.getText().toString();
                if (btText.equals("Skeniraj")) {
                    if(tbLocation.getText().toString().equals("")) {
                        Toast.makeText(InventoryActivity.this, "Lokacija mora biti izbrana", Toast.LENGTH_SHORT).show();
                    } else {
                        FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                        fixedAssetsFragment.startScanning();
                        btToggleScanning.setText("Prekini");
                    }
                } else {
                    FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                    fixedAssetsFragment.stopScanning();
                    fixedAssetsFragment.sortBasedOnLocation(tbLocation.getText().toString());
                    btToggleScanning.setText("Skeniraj");
                }
            }
        });

        tbLocation.setText("");
        tbLocation.requestFocus();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedLocation = sharedPreferences.getString("location_inventory", "");
        tbLocation.setText(savedLocation);
        tbLocation.clearFocus();
    }

    private void checkLocationsServer() {
        if(scannedIndex == scanned.size()) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            Intent myIntent = new Intent(getApplicationContext(), InventoryActivity.class);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("location_inventory", tbLocation.getText().toString());
            editor.apply();
            startActivity(myIntent);
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String url = sharedPreferences.getString("url", "");
            String token = sharedPreferences.getString("token", "");
            LocalDate localDate = LocalDate.now();
            Communicator communicator = new Communicator();
            ItemLocation item = findItemByEpc(realItems, scanned.get(scannedIndex).getEcd());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    duplicate = communicator.checkDuplicate(url, token, item.getEcd());
                } catch (final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Sentry.captureException(e);
                        }
                    });
                }
            }
        });
        backgroundThread.start(); // Start the thread
        try {
            backgroundThread.join(); // Wait for the thread to finish
            if (duplicate.isSuccess() && !duplicate.getLocation().isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Izbirnik lokacije");
                builder.setMessage("Sredstvo: \nNaziv: "+item.getName()+"\nZadolžen: " + item.getCaretaker()  +
                        "\nJe bilo skenirano na lokaciji: " + duplicate.getLocation() + ". \nKatera lokacija je prava?");
                builder.setPositiveButton(tbLocation.getText().toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CheckOut checkOutItem = new CheckOut(-1, item.getQid(), item.getItem(), tbLocation.getText().toString(),
                                item.getCode(), item.getEcd(), item.getName(), "", localDate.toString(), 5, "", -1, timestamp.toString(), 5, timestamp.toString(), 5, "");
                        checkOutViewModel.insert(checkOutItem);
                        ItemLocation toUpdate = item;
                        toUpdate.setLocation(tbLocation.getText().toString());
                        itemLocationViewModel.update(toUpdate);
                        scannedIndex +=1;
                        checkLocationsServer();
                    }
                });
                builder.setNegativeButton(duplicate.getLocation().toString(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CheckOut checkOutItem = new CheckOut(-1, item.getQid(), item.getItem(), duplicate.getLocation().toString(),
                                item.getCode(), item.getEcd(), item.getName(), "", localDate.toString(), 5, "", -1, timestamp.toString(), 5, timestamp.toString(), 5, "");
                        checkOutViewModel.insert(checkOutItem);
                        ItemLocation toUpdate = item;
                        toUpdate.setLocation(duplicate.getLocation().toString());
                        itemLocationViewModel.update(toUpdate);
                        scannedIndex +=1;
                        checkLocationsServer();
                    }
                });
                // Display the dialog for the current item
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // In the case there is no other location just save the asset.
                CheckOut checkOutItem = new CheckOut(-1, item.getQid(), item.getItem(), tbLocation.getText().toString(),
                        item.getCode(), item.getEcd(), item.getName(), "", localDate.toString(), 5, "", -1, timestamp.toString(), 5, timestamp.toString(), 5, "");
                checkOutViewModel.insert(checkOutItem);
                ItemLocation toUpdate = item;
                toUpdate.setLocation(tbLocation.getText().toString());
                itemLocationViewModel.update(toUpdate);
                scannedIndex +=1;
                checkLocationsServer();
            }
        } catch (InterruptedException e) {
               Sentry.captureException(e);
            }
        }
    }

    private ItemLocation findItemByEpc(List<ItemLocation> items, String epc) {
        for(ItemLocation item: items) {
            if(item.getEcd().equals(epc)) {
                return item;
            }
        }
        return null;
    }

    private void initializeFragment() {
        // Initialize the Fixed Assets Fragment
        replaceFragment(new FixedAssetsFragment());
    }
    // Replaces a fragment
    private void replaceFragment(Fragment fragment) {
        // Data for knowing who is the caller
        Bundle arguments = new Bundle();
        arguments.putString( "callerID" , "InventoryActivity");
        fragment.setArguments(arguments);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void getResult(String result) {
        if(result != null && !result.trim().isEmpty()) {
            tbLocation.setText(result);
        }
    }
}