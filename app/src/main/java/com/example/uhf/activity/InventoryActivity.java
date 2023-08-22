package com.example.uhf.activity;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.barcode.Barcode;
import com.example.uhf.barcode.BarcodeUtility;
import com.example.uhf.custom.CustomSearchableSpinner;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.fragment.KeyDwonFragment;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.ViewModel.CheckOutViewModel;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.mvvm.ViewModel.LocationViewModel;
import com.microsoft.appcenter.analytics.Analytics;
import com.rscja.deviceapi.RFIDWithUHFUART;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends FragmentActivity implements Barcode {
private ItemViewModel itemViewModel;
private Button btConfirm;
public KeyDwonFragment currentFragment=null;
public EditText tbLocation;
public CustomSearchableSpinner cbLocation;
public String currentLocation;
private Button btToggleScanning;
public RFIDWithUHFUART mReader;
    private BarcodeUtility barcodeUtility;
    private ArrayAdapter locationsAdapter;
    private LocationViewModel locationsViewModel;
    private Button btExit;
    public ItemTemporary current;
    private SearchView swListing;
    private String location;
    private CheckOutViewModel checkOutViewModel;
    private List<CheckOut> checkOutItems;

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
            Analytics.trackEvent(e.getLocalizedMessage());
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


        btConfirm = findViewById(R.id.btConfirm);
        tbLocation = findViewById(R.id.tbLocation);

        btToggleScanning = findViewById(R.id.btToggleScanning);
        cbLocation = findViewById(R.id.cbLocation);
        cbLocation.setTitle("Izberite lokacijo");
        cbLocation.setPositiveButton("Potrdi");
        List<String> locations = new ArrayList<String>();
        locationsAdapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item,locations);
        locationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        locationsViewModel = ViewModelProviders.of(InventoryActivity.this).get(LocationViewModel.class);
        cbLocation.setAdapter(locationsAdapter);
        locationsViewModel.getAllItems().observe(InventoryActivity.this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> items) {
                List<String> locations = new ArrayList<String>();
                // Think about improving the time complexity here
                for (Location location : items) {
                    locations.add(location.getLocation());
                }


                locationsAdapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item,locations);
                cbLocation.setAdapter(locationsAdapter);

            }
        });
        cbLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cbLocation.isSpinnerDialogOpen = false;

                // Change the location text
                if(helpCounter != 0) {
                    tbLocation.setText(cbLocation.getSelectedItem().toString());
                    currentLocation = tbLocation.getText().toString();
                }
                helpCounter += 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cbLocation.isSpinnerDialogOpen = false;
            }





        });



        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                fixedAssetsFragment.stopScanning();


                List<ItemTemporary> scanned = fixedAssetsFragment.temporaryAdapter.items;
                List<ItemLocation> realItems = fixedAssetsFragment.itemsClassLevel;
                LocalDate localDate = LocalDate.now();

                for (int i = 0; i < scanned.size(); i++) {
                    ItemLocation item = findItemByEpc(realItems, scanned.get(i).getEcd());
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    CheckOut checkOutItem = new CheckOut(-1, item.getQid(), item.getItem(), currentLocation,
                            item.getCode(), item.getEcd(), item.getName(), "", localDate.toString(),  5,  "", -1, timestamp.toString(), 5, timestamp.toString(), 5,  "");
                    checkOutViewModel.insert(checkOutItem);

                }

                Intent myIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                startActivity(myIntent);


                /*

                if(current!=null) {
                Intent myIntent = new Intent(getApplicationContext(), LocationActivity.class);
                myIntent.putExtra("id", current.getID());
                myIntent.putExtra("epc", current.getEcd());
                myIntent.putExtra("callerID", "InventoryProcessLocation");
                String location = tbLocation.getText().toString();
                myIntent.putExtra("location", tbLocation.getText().toString());
                // Redirect to the location activity
                startActivity(myIntent);

                } */




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



        int postest = locationsAdapter.getPosition(location);

        if(location!=null) {
            cbLocation.setSelection(locationsAdapter.getPosition(location));
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