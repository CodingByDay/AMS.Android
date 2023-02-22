package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.fragment.UHFLocationFragment;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.rscja.deviceapi.RFIDWithUHFUART;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    private Button btExit;
    private Button btRequest;

    public Item currentItem;

    private boolean fLocate;

    public RFIDWithUHFUART mReader;

    public List<ItemTemporary> scannedItems =  new ArrayList<ItemTemporary>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        initializeFragment();
        initializeActivity();
        initUHF();
    }

    private void initializeActivity() {
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
                replaceFragment(new UHFLocationFragment());
            }
        });
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