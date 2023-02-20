package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.fragment.KeyDwonFragment;
import com.example.uhf.fragment.UHFReadTagFragment;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.entity.UHFTAGInfo;

import java.io.File;
import java.util.Objects;

public class InventoryActivity extends FragmentActivity  {
private ItemViewModel itemViewModel;
private Button btConfirm;
public KeyDwonFragment currentFragment=null;

private EditText tbLocation;
private Button btToggleScanning;
public RFIDWithUHFUART mReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
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

    private void initViews() {
        btConfirm = findViewById(R.id.btConfirm);
        tbLocation = findViewById(R.id.tbLocation);
        btToggleScanning = findViewById(R.id.btToggleScanning);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO add loader while data is being filtered
                FixedAssetsFragment.getInstance().sortBasedOnLocation(tbLocation.getText().toString());

            }
        });
        btToggleScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btText = btToggleScanning.getText().toString();
                if (btText.equals("Skeniraj")) {
                    FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                    fixedAssetsFragment.startScanning();
                    btToggleScanning.setText("Nehaj");
                } else {
                    FixedAssetsFragment fixedAssetsFragment = FixedAssetsFragment.getInstance();
                    fixedAssetsFragment.stopScanning();
                    btToggleScanning.setText("Skeniraj");
                }
            }
        });


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

}