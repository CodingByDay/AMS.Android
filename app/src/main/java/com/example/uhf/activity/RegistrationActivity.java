package com.example.uhf.activity;

import androidx.appcompat.app.ActionBar;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.barcode.Barcode;
import com.example.uhf.barcode.BarcodeUtility;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.fragment.UHFLocationFragment;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.view.UhfLocationCanvasView;
import com.microsoft.appcenter.analytics.Analytics;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity implements Barcode {
    private Button btExit;
    private Button btRequest;
    public UhfLocationCanvasView llChart;
    public ItemLocation currentItem;

    private boolean fLocate;

    public RFIDWithUHFUART mReader;

    public List<ItemTemporary> scannedItems =  new ArrayList<ItemTemporary>();
    private ProgressDialog mypDialog;

    private EditText tbBarcodeScan;
    private SearchView swListing;
    private BarcodeUtility barcodeUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.riko_toolbar);
        new Thread(new Runnable() {

            @Override
            public void run() {
                RegistrationActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        initUHF();
                        initializeFragment();
                        initializeActivity();
                    }
                });
            }
        }).start();

    }

    private void initializeActivity() {
        tbBarcodeScan = findViewById(R.id.tbBarcodeScan);
        barcodeUtility = new BarcodeUtility(this, this);
        tbBarcodeScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                fixedAssetsFragment.adapterLocation.findIdent(charSequence.toString());
                } catch (Exception err) {
                    Analytics.trackEvent(err.getLocalizedMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

                fixedAssetsFragment.adapterLocation.searchByField(currentColumnSearch, newText);
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
                Toast.makeText(RegistrationActivity.this, "Približite tag", Toast.LENGTH_SHORT).show();

                boolean doWhile = true;
                while (doWhile) {
                    UHFTAGInfo tag = mReader.inventorySingleTag();
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                    float floatValue = -1000;
                    try {
                        Number parsedNumber = decimalFormat.parse(tag.getRssi());
                        floatValue = parsedNumber.floatValue();
                        System.out.println("Parsed float value: " + floatValue);
                    } catch (ParseException e) {

                    }
                    if (floatValue > - 33) {


                        Intent myIntent = new Intent(getApplicationContext(), LocationActivity.class);

                        myIntent.putExtra("epc", tag.getEPC());
                        myIntent.putExtra("callerID", "Registration");
                        myIntent.putExtra("item_id", currentItem.getID());

                        mReader.stopLocation();
                        startActivity(myIntent);
                        finish();
                        break;
                        // Comment
                    }
                }
            }
        });
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F1: {
                btExit.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F2: {
                btRequest.performClick();
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

        }
        return super.onKeyDown(keyCode, event);
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

    @Override
    public void getResult(String result) {
        if(tbBarcodeScan.hasFocus()) {
            tbBarcodeScan.setText(result);
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