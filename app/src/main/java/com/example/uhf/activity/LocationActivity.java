package com.example.uhf.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.barcode.Barcode;
import com.example.uhf.barcode.BarcodeUtility;
import com.example.uhf.custom.CustomSearchableSpinner;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationCache;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.ViewModel.CheckOutViewModel;
import com.example.uhf.mvvm.ViewModel.ItemLocationCacheViewModel;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.mvvm.ViewModel.LocationViewModel;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;
import com.example.uhf.tools.SettingsHelper;
import com.example.uhf.tools.UIHelper;
import com.example.uhf.view.UhfLocationCanvasView;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.interfaces.IUHF;
import com.rscja.deviceapi.interfaces.IUHFLocationCallback;
import com.rscja.team.qcom.deviceapi.P;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class LocationActivity extends AppCompatActivity implements Barcode {

    Dialog dialog;
    private UHFMainActivity mContext;
    private UhfLocationCanvasView llChart;
    private EditText etEPC;
    private Button btStart,btStop;
    final int EPC=2;
    private RFIDWithUHFUART mReader;
    private ItemViewModel itemViewModel;
    private List<Item> itemsClassLevel;
    private TextView lbItem;
    private int id;
    private Button btToggle;

    private BarcodeUtility barcodeUtility;
    private LocationViewModel locationsViewModel;
    private ItemLocationCacheViewModel itemLocationCacheViewModel;
    private List<Location> locations;
    private ArrayAdapter locationsAdapter;
    private ItemLocation item;
    private ItemLocation locationItem;

    private ItemLocationViewModel itemLocationViewModel;
    private List<ItemLocation> itemsLocationClassLevel;
    private List<ItemLocation> itemsLocationsClassLevel;
    private ItemLocation currentItem;
    private String epc;
    private String callerID;
    private String location;
    private SettingsViewModel settingsView;
    private List<Setting> settingsList;
    private int idValue;

    private CheckOutViewModel checkOutViewModel;
    private List<CheckOut> checkOutItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initUHF();
        initSettings();

        barcodeUtility = new BarcodeUtility(this, this);
        llChart=findViewById(R.id.llChart);
        etEPC=findViewById(R.id.etEPC);
        btToggle=findViewById(R.id.btToggle);
        btStop=findViewById(R.id.btStop);
        lbItem = findViewById(R.id.lbItem);
        btToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               toggleLocation(false);
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(myIntent);
            }
        });
        llChart.setData(1);
        llChart.clean();
        Bundle extras = getIntent().getExtras();
        callerID =  extras.getString("callerID");

        resolveCaller(callerID);


    }



    private ItemLocation findCorrectItemLocation(String epc) {
        if(itemsLocationClassLevel!=null) {
        for(ItemLocation item: itemsLocationClassLevel) {
            if (item.getEcd().equals(epc)) {
                return item;
            }
        }
        }
        return null;
    }
    private void resolveCaller(String callerID) {
        if(callerID.equals("InventoryProcessLocation")) {
            initialize();
            Bundle extras = getIntent().getExtras();
            epc =  extras.getString("epc");
            etEPC.setText(epc);
            location = extras.getString("location");
            idValue = extras.getInt("id");
            startLocation();

        } else if (callerID.equals("Registration")) {
            initialize();
            // ItemTemporary strongest = findStrongestSignal();
            Bundle extras = getIntent().getExtras();
            String strongest =  extras.getString("epc");
            id = extras.getInt("item_id");
            etEPC.setText(strongest);

        }
    }


    private void toggleLocation(boolean start) {
        if(start) {
            startLocation();
            btToggle.setText("Pavza - F1");
        }
        else {
        if (btToggle.getText().equals("Pavza - F1")) {
            stopLocation();
            btToggle.setText("Nadaljuj - F1");
        } else {
            startLocation();
            btToggle.setText("Pavza - F1");
        }}
    }
    private ItemLocation findById(Integer id) {
        for(ItemLocation item : itemsLocationClassLevel) {
            if(item.getID()==id) {
                return item;
            }
        }
        return null;
    }

    private void initialize() {
        itemLocationViewModel = ViewModelProviders.of(this).get(ItemLocationViewModel.class);
        itemLocationViewModel.getAllItems().observe(this, new Observer<List<ItemLocation>>() {
            @Override
            public void onChanged(List<ItemLocation> items) {
                currentItem = findCorrectItemLocation(epc);
                itemsLocationClassLevel = items;
                item = findById(id);
                if(item!=null) {
                    lbItem.setText(item.getName());
                    toggleLocation(false);
                    toggleLocation(true);
                }

            }
        });

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemsClassLevel = items;

            }
        });
        itemLocationCacheViewModel = ViewModelProviders.of(this).get(ItemLocationCacheViewModel.class);
        // ItemLocation view model




        checkOutViewModel = ViewModelProviders.of(this).get(CheckOutViewModel.class);
        checkOutViewModel.getAllItems().observe(this, new Observer<List<CheckOut>>() {
            @Override
            public void onChanged(List<CheckOut> checkOuts) {
                checkOutItems = checkOuts;
            }
        });
    }

    @Override
    public void getResult(String result) {
        if(result != null && !result.isEmpty()) {
            if(dialog!=null && dialog.isShowing()) {
                EditText tbLocationScan = dialog.findViewById(R.id.tbLocationScan);
                if(tbLocationScan.hasFocus()) {
                    // TODO: Check if the location is correct
                    tbLocationScan.setText(result);
                }
            }
        }

    }

    private ItemLocation getItemByEpc(String epc) {
        for (ItemLocation item: itemsLocationsClassLevel) {
            if(item.getEcd().equals(epc)) {
                return item;
            }
        }
        return null;
    }

    private String locationCurrent;
    public class LocationDialog {
        public void showDialog(AppCompatActivity activity) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.add_location_alert);
            EditText tbLocationScan = (EditText) dialog.findViewById(R.id.tbLocationScan);
            tbLocationScan.setShowSoftInputOnFocus(false);
            tbLocationScan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(!charSequence.equals("") ) {
                        locationCurrent = charSequence.toString();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            CustomSearchableSpinner cbLocation = (CustomSearchableSpinner) dialog.findViewById(R.id.cbLocation);
            cbLocation.setTitle("Izberite lokaciju");
            cbLocation.setPositiveButton("Potrdi");
            Button btYes = (Button) dialog.findViewById(R.id.btYes);
            Button btNo = (Button) dialog.findViewById(R.id.btNo);
            // dialog dismiss
            btYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Date date = new Date(System.currentTimeMillis());

                    // Add the cached item here
                    FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                    ItemLocation item = fixedAssetsFragment.itemLocationCurrent;


                    item.setEcd(locationItem.getEcd());
                    item.setLocation(locationCurrent);
                    item.setTimestamp(date.toString());
                    item.setUser(SettingsHelper.SettingsHelp.returnSettingValue(settingsList, "user"));


                     itemLocationViewModel.update(item);


                    //itemLocationCacheViewModel.insert(new ItemLocationCache(fixedAssetsFragment.itemLocationCurrent.getID(), locationItem.getItem(), locationItem.getCode(), locationItem.getLocation(), locationItem.getEcd(), locationItem.getName(), date.toString(), SettingsHelper.SettingsHelp.returnSettingValue(settingsList, "user"),locationItem.getQid()));
                    // Return back to the list - Registration activity
                    dialog.dismiss();       // dismiss
                    Intent myIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    startActivity(myIntent);
                }
            });
            btNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Save the item to the
                    locationItem.setLocation("");
                    Date date = new Date(System.currentTimeMillis());
                    // Add the cached item here
                    FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();

                    item.setEcd(locationItem.getEcd());
                    item.setLocation("");
                    item.setTimestamp(date.toString());
                    item.setUser(SettingsHelper.SettingsHelp.returnSettingValue(settingsList, "user"));


                    itemLocationViewModel.update(item);

                    // Return back to the list - Registration activity
                    dialog.dismiss();       // dismiss
                    Intent myIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    startActivity(myIntent);
                }
            });

            dialog.show();
            locations = new ArrayList<>();
            locationsAdapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item,locations);
            locationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            locationsViewModel = ViewModelProviders.of(LocationActivity.this).get(LocationViewModel.class);
            cbLocation.setAdapter(locationsAdapter);
            locationsViewModel.getAllItems().observe(LocationActivity.this, new Observer<List<Location>>() {
                @Override
                public void onChanged(List<Location> items) {
                    List<String> locations = new ArrayList<String>();
                    // Think about improving the time complexity here
                    for (Location location : items) {
                        locations.add(location.getLocation());
                    }

                    if (dialog!=null && dialog.isShowing()) {
                        locationsAdapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_spinner_item,locations);
                        SearchableSpinner cbLocation = dialog.findViewById(R.id.cbLocation);
                        cbLocation.setAdapter(locationsAdapter);
                    }
                }
            });
            cbLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    // Change the location text
                    cbLocation.isSpinnerDialogOpen = false;
                    tbLocationScan.setText(cbLocation.getSelectedItem().toString());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    cbLocation.isSpinnerDialogOpen = false;

                }
            });
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

    private ItemLocation findItemByEpc(String epc) {
        for(ItemLocation item: itemsLocationClassLevel) {
            if(item.getEcd().equals(epc)) {
                return item;
            }
        }
        return null;
    }
    private void startLocation(){
        if(etEPC.getText().toString().equals("")) {
            return;
        }
        String epc=etEPC.getText().toString();
        boolean result= mReader.startLocation(this,epc, IUHF.Bank_EPC,32, new IUHFLocationCallback(){
            @Override

            public void getLocationValue(int Value) {
                llChart.setData(Value);
                if(Value >= 95) {
                    stopLocation();
                    AlertDialog.Builder alert = new AlertDialog.Builder(LocationActivity.this);
                    alert.setTitle("Potrditev oznake");
                    if (callerID.equals("InventoryProcessLocation")) {
                        alert.setMessage("Ali želite potrditi inventuro?");
                    } else {
                        alert.setMessage("Ali želite povezati oznako z sredstvom?");
                    }
                    alert.setMessage("Ali želite povezati oznako z sredstvom?");
                    alert.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (callerID.equals("InventoryProcessLocation")) {
                                String epc = LocationActivity.this.epc;
                                String location = LocationActivity.this.location;
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                // Consider this
                                ItemLocation item = findItemByEpc(epc);
                                assert item != null;
                                if(item.getCode()!=null) {
                                    String code = item.getCode();
                                }
                                String name = item.getName();
                                int qid = item.getQid();
                                FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                                ItemLocation itemest = fixedAssetsFragment.itemLocationCurrent;

                                LocalDate localDate = LocalDate.now();

                                // itemLocationViewModel.update(item);
                                CheckOut checkOutItem = new CheckOut(-1, item.getQid(), item.getItem(), location,
                                        item.getCode(), item.getEcd(), item.getName(), "", localDate.toString(),  5,  "", -1, timestamp.toString(), 5, timestamp.toString(), 5, "");

                                checkOutViewModel.insert(checkOutItem);

                                Intent myIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                                myIntent.putExtra("epc", location);
                                startActivity(myIntent);
                                // Continues here
                            } else {
                                // Transfer location and make a new object
                                dialog.dismiss();
                                stopLocation();
                                String item = LocationActivity.this.item.getItem();
                                String code = LocationActivity.this.item.getCode();
                                String name = LocationActivity.this.item.getName();
                                int qid = LocationActivity.this.item.getQid();
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                String location = "";
                                String epc = etEPC.getText().toString();
                                // TODO: Link item name here
                                locationItem = new ItemLocation(item, code, location, epc, name, timestamp.toString(), SettingsHelper.SettingsHelp.returnSettingValue(settingsList, "user"), qid);
                                LocationDialog alertLocation = new LocationDialog();
                                alertLocation.showDialog(LocationActivity.this);
                            }

                        }
                    });
                    alert.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startLocation();
                        }
                    });
                    alert.show();
                }
            }
        });
        if(!result) {
           stopLocation();
           startLocation();
        }
        etEPC.setEnabled(false);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_F1:
            {
                btToggle.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F2:
            {
                btStop.performClick();
                return true;
            }
            case KeyEvent.KEYCODE_F3:
            {

                return true;
            }
            case KeyEvent.KEYCODE_F4:
            {

                return true;
            }
            case KeyEvent.KEYCODE_F5:
            {
                //your Action code
                return true;
            }
            case KeyEvent.KEYCODE_F6:
            {
                //your Action code
                return true;
            }
            case KeyEvent.KEYCODE_F7:
            {
                //your Action code
                return true;
            }
            case KeyEvent.KEYCODE_F8:
            {

                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initUHF() {
        try {
            mReader = RFIDWithUHFUART.getInstance();
        } catch (Exception ex) {
            initUHF();
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
                Toast.makeText(LocationActivity.this, "init fail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mypDialog = new ProgressDialog(LocationActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }

    public void stopLocation(){
        mReader.stopLocation();
        etEPC.setEnabled(true);
    }

}