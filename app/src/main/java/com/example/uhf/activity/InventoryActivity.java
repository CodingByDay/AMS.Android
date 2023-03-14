package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.fragment.KeyDwonFragment;
import com.example.uhf.fragment.UHFReadTagFragment;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.mvvm.ViewModel.LocationViewModel;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryActivity extends FragmentActivity implements Barcode {
private ItemViewModel itemViewModel;
private Button btConfirm;
public KeyDwonFragment currentFragment=null;

public EditText tbLocation;
public SearchableSpinner cbLocation;
private String currentLocation;
private Button btToggleScanning;
public RFIDWithUHFUART mReader;
    private BarcodeUtility barcodeUtility;
    private ArrayAdapter locationsAdapter;
    private LocationViewModel locationsViewModel;

    public ItemTemporary current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        barcodeUtility = new BarcodeUtility(this, this);
        initViews();
        initializeFragment();
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 296) {
            // This is the hardware start button
            FixedAssetsFragment.getInstance().toggleScanning(btToggleScanning.getText().equals("Skeniraj"));
        }
        return super.onKeyDown(keyCode, event);
    }
    private int helpCounter = 0;
    private void initViews() {

        btConfirm = findViewById(R.id.btConfirm);
        tbLocation = findViewById(R.id.tbLocation);
        tbLocation.setBackgroundColor(Color.parseColor("#34e5eb"));
        btToggleScanning = findViewById(R.id.btToggleScanning);
        cbLocation = findViewById(R.id.cbLocation);
        cbLocation.setTitle("Izberite lokaciju");
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
                // Change the location text
                if(helpCounter != 0) {
                    tbLocation.setText(cbLocation.getSelectedItem().toString());
                    currentLocation = tbLocation.getText().toString();
                }
                helpCounter += 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO add loader while data is being filtered
                if(current!=null) {
                    Intent myIntent = new Intent(getApplicationContext(), LocationActivity.class);
                    myIntent.putExtra("epc", current.getEcd());
                    myIntent.putExtra("callerID", "InventoryProcessLocation");
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
                        btToggleScanning.setText("Nehaj");
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