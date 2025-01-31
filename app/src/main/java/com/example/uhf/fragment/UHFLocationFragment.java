package com.example.uhf.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.activity.RegistrationActivity;
import com.example.uhf.activity.UHFMainActivity;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.tools.UIHelper;
import com.example.uhf.view.UhfLocationCanvasView;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.interfaces.IUHF;
import com.rscja.deviceapi.interfaces.IUHFLocationCallback;


public class UHFLocationFragment extends KeyDwonFragment {

    private static UHFLocationFragment instance;
    String TAG="UHF_LocationFragment";
    private RegistrationActivity mContext;
    public UhfLocationCanvasView llChart;
    private EditText etEPC;
    private Button btStart,btStop;
    final int EPC=2;
    private RFIDWithUHFUART mReader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uhflocation, container, false);
        initContext(view);
        instance = this;
        return view;
    }




    public static UHFLocationFragment getInstance() {
        return instance;
    }
    private void initContext(View view) {
        mContext = (RegistrationActivity) getActivity();
        llChart=view.findViewById(R.id.llChart);
        etEPC=view.findViewById(R.id.etEPC);
        btStart=view.findViewById(R.id.btStart);
        btStop=view.findViewById(R.id.btStop);

        mContext.llChart = llChart;
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
        getView().post(new Runnable() {
            @Override
            public void run() {
                llChart.clean();
                ItemTemporary strongest = findStrongestSignal();
                etEPC.setText(strongest.getEcd());
            }
        });

    }

    private ItemTemporary findStrongestSignal() {
        ItemTemporary itemStrongest = new ItemTemporary("-200");
        for(ItemTemporary itemTemporary: mContext.scannedItems) {
            float rssi = Float.parseFloat(itemTemporary.getRssi().replace(",", "."));
            if (rssi > Float.parseFloat(itemStrongest.getRssi().replace(",", "."))) {
                itemStrongest = itemTemporary;
            }
        }
        return itemStrongest;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopLocation();
    }
    @Override
    public void myOnKeyDwon() {
        if(btStart.isEnabled()) {
            startLocation();
        }else{
            stopLocation();
        }
    }
    private void startLocation(){
        String epc=etEPC.getText().toString();
        boolean result= mReader.startLocation(requireContext(), epc,IUHF.Bank_EPC,32, new IUHFLocationCallback(){
            @Override
            public void getLocationValue(int Value) {
                llChart.setData(Value);
            }
        });
        if(!result){

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
                Toast.makeText(getActivity(), "RFID module not present", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mypDialog = new ProgressDialog(getActivity());
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
