package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.uhf.R;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;

import java.io.File;
import java.util.Objects;

public class InventoryActivity extends AppCompatActivity {
private ItemViewModel itemViewModel;
private Button btConfirm;
private EditText tbLocation;
private Button btToggleScanning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initViews();
        initializeFragment();
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