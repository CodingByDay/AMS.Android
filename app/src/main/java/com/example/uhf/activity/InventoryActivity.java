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
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.mvvm.ViewModel.LocationViewModel;
import com.microsoft.appcenter.analytics.Analytics;
import com.rscja.deviceapi.RFIDWithUHFUART;

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
                Intent myIntent = new Intent(getApplicationContext(), EntryInitialActivity.class);
                startActivity(myIntent);
                finishAffinity();

            }
        });
        btConfirm = findViewById(R.id.btConfirm);
        tbLocation = findViewById(R.id.tbLocation);
        tbLocation.setBackgroundColor(Color.parseColor("#34e5eb"));
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
                    locations.add(location.getName());
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
                // TODO add loader while data is being filtered
                if(current!=null) {
                    Intent myIntent = new Intent(getApplicationContext(), LocationActivity.class);
                    myIntent.putExtra("id", current.getID());
                    myIntent.putExtra("epc", current.getEcd());
                    myIntent.putExtra("callerID", "InventoryProcessLocation");
                    String location = tbLocation.getText().toString();
                    myIntent.putExtra("location", tbLocation.getText().toString());

                    // Redirect to the location activity
                    startActivity(myIntent);
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



        int postest = locationsAdapter.getPosition(location);

        if(location!=null) {
            cbLocation.setSelection(locationsAdapter.getPosition(location));
        }
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