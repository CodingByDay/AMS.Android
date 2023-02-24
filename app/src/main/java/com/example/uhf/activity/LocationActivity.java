package com.example.uhf.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.tools.UIHelper;
import com.example.uhf.view.UhfLocationCanvasView;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.interfaces.IUHF;
import com.rscja.deviceapi.interfaces.IUHFLocationCallback;

import java.sql.Array;
import java.util.List;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initUHF();
        llChart=findViewById(R.id.llChart);
        etEPC=findViewById(R.id.etEPC);
        btStart=findViewById(R.id.btStart);
        btStop=findViewById(R.id.btStop);
        lbItem = findViewById(R.id.lbItem);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocation();
            }
        });
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocation();
            }
        });
        llChart.clean();
        initialize();
        // ItemTemporary strongest = findStrongestSignal();
        Bundle extras = getIntent().getExtras();
        String strongest =  extras.getString("epc");
        int id = extras.getInt("item_id");
        etEPC.setText(strongest);
        Item item = findById(id);
        if(item!=null) {
            lbItem.setText(item.getID());
        }
    }

    private Item findById(Integer id) {
        for(Item item :  itemsClassLevel) {
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
            }
        });
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
                        }
                    });
                    alert.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            stopLocation();
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
        btStart.setEnabled(false);
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
        btStart.setEnabled(true);
        etEPC.setEnabled(true);
    }

}