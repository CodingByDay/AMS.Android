package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.tools.UIHelper;
import com.example.uhf.view.UhfLocationCanvasView;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.interfaces.IUHF;
import com.rscja.deviceapi.interfaces.IUHFLocationCallback;

import java.sql.Array;

public class LocationActivity extends AppCompatActivity {

    String TAG="UHF_LocationFragment";
    private UHFMainActivity mContext;
    private UhfLocationCanvasView llChart;
    private EditText etEPC;
    private Button btStart,btStop;
    final int EPC=2;
    private RFIDWithUHFUART mReader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initUHF();
        llChart=findViewById(R.id.llChart);
        etEPC=findViewById(R.id.etEPC);
        btStart=findViewById(R.id.btStart);
        btStop=findViewById(R.id.btStop);
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
        //ItemTemporary strongest = findStrongestSignal();
        Bundle extras = getIntent().getExtras();
        String strongest =  extras.getString("epc");
        etEPC.setText(strongest);


    }

//    private ItemTemporary findStrongestSignal() {
//        ItemTemporary itemStrongest = new ItemTemporary("-200");
//        for(ItemTemporary itemTemporary: mContext.scannedItems) {
//            float rssi = Float.parseFloat(itemTemporary.getRssi().replace(",", "."));
//            if (rssi > Float.parseFloat(itemStrongest.getRssi().replace(",", "."))) {
//                itemStrongest = itemTemporary;
//            }
//        }
//        return itemStrongest;
//    }
    private void startLocation(){
        String epc=etEPC.getText().toString();
        boolean result= mReader.startLocation(this,epc, IUHF.Bank_EPC,32, new IUHFLocationCallback(){
            @Override
            public void getLocationValue(int Value) {
                llChart.setData(Value);
            }
        });
        if(!result){
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