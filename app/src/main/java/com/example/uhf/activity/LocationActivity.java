package com.example.uhf.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.tools.UIHelper;
import com.example.uhf.view.UhfLocationCanvasView;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.interfaces.IUHF;
import com.rscja.deviceapi.interfaces.IUHFLocationCallback;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.List;
import java.util.Objects;

public class LocationActivity extends AppCompatActivity {

    String TAG="UHF_LocationFragment";
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initUHF();
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

        llChart.clean();
        initialize();
        // ItemTemporary strongest = findStrongestSignal();
        Bundle extras = getIntent().getExtras();
        String strongest =  extras.getString("epc");
        id = extras.getInt("item_id");
        etEPC.setText(strongest);

    }



    private void toggleLocation(boolean start) {
        if(start) {
            startLocation();
            btToggle.setText("Pavza");
        }
        else {
        if (btToggle.getText().equals("Pavza")) {
            stopLocation();
            btToggle.setText("Nadaljuj");
        } else {
            startLocation();
            btToggle.setText("Pavza");
        }}
    }
    private Item findById(Integer id) {
        for(Item item : Objects.requireNonNull(itemViewModel.getAllItems().getValue())) {
            if(item.getID()==id) {
                return item;
            }
        }
        return null;
    }

    private void initialize() {
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemsClassLevel = items;
                Item item = findById(id);
                if(item!=null) {
                    lbItem.setText(item.getName());
                    toggleLocation(true);
                }
            }
        });
    }

    public class LocationDialog {
        public void showDialog(AppCompatActivity activity){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.add_location_alert);
            EditText tbLocationScan = (EditText) dialog.findViewById(R.id.tbLocationScan);
            SearchableSpinner cbLocation = (SearchableSpinner) dialog.findViewById(R.id.cbLocation);
            Button btYes = (Button) dialog.findViewById(R.id.btYes);
            Button btNo = (Button) dialog.findViewById(R.id.btNo);
            // dialog dismiss
            btYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            btNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            cbLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            dialog.show();
        }
    }
    private void startLocation(){
        String epc=etEPC.getText().toString();
        boolean result= mReader.startLocation(this,epc, IUHF.Bank_EPC,32, new IUHFLocationCallback(){
            @Override
            public void getLocationValue(int Value) {
                llChart.setData(Value);
                if(Value >= 100) {
                    stopLocation();
                    AlertDialog.Builder alert = new AlertDialog.Builder(LocationActivity.this);
                    alert.setTitle("Potrditev oznake");
                    alert.setMessage("Ali želite povezati oznako z sredstvom?");
                    alert.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            stopLocation();
                            LocationDialog alert = new LocationDialog();
                            alert.showDialog(LocationActivity.this);
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
            UIHelper.ToastMessage(this, R.string.psam_msg_fail);
            return;
        }
        etEPC.setEnabled(false);
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