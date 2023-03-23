package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.fragment.UHFLocationFragment;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.view.UhfLocationCanvasView;
import com.rscja.deviceapi.RFIDWithUHFUART;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    private Button btExit;
    private Button btRequest;
    public UhfLocationCanvasView llChart;
    public ItemLocation currentItem;

    private boolean fLocate;

    public RFIDWithUHFUART mReader;

    public List<ItemTemporary> scannedItems =  new ArrayList<ItemTemporary>();
    private ProgressDialog mypDialog;

    private SearchView swListing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        initUHF();
        initializeFragment();
        initializeActivity();

    }

    private void initializeActivity() {
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
        btExit = findViewById(R.id.btExit);
        btRequest = findViewById(R.id.btRequest);
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), EntryInitialActivity.class);
                startActivity(myIntent);
            }
        });
        btRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();

                if(currentItem == null) {
                    Toast.makeText(RegistrationActivity.this, "Sredstvo ni izbrano?!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mypDialog = new ProgressDialog(RegistrationActivity.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mypDialog.setMessage("Iskanje najmočnejšega signala. Počakajte...");
                mypDialog.setCanceledOnTouchOutside(false);
                mypDialog.show();
                fixedAssetsFragment.startScanning();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fixedAssetsFragment.stopScanning();
                        Intent myIntent = new Intent(getApplicationContext(), LocationActivity.class);
                        String strongest = findStrongestSignal().getEcd();
                        
                        if(strongest!=null) {
                        myIntent.putExtra("epc", strongest);
                        myIntent.putExtra("callerID", "Registration");
                        myIntent.putExtra("item_id", currentItem.getID());
                        mypDialog.cancel();
                        startActivity(myIntent);
                        } else {
                            mypDialog.cancel();
                            Toast.makeText(RegistrationActivity.this, "V bližini ni signala", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 5000);
            }
        });
    }
        private ItemTemporary findStrongestSignal() {
        ItemTemporary itemStrongest = new ItemTemporary("-200");
        for(ItemTemporary itemTemporary: this.scannedItems) {
            float rssi = Float.parseFloat(itemTemporary.getRssi().replace(",", "."));
            if (rssi > Float.parseFloat(itemStrongest.getRssi().replace(",", "."))) {
                itemStrongest = itemTemporary;
            }
        }
        return itemStrongest;
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
                Toast.makeText(RegistrationActivity.this, "init fail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mypDialog = new ProgressDialog(RegistrationActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
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
        arguments.putString( "callerID" , "RegistrationActivity");
        fragment.setArguments(arguments);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}