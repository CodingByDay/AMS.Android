package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.uhf.R;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;

import java.util.Objects;

public class InventoryActivity extends AppCompatActivity {
private ItemViewModel itemViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initializeFragment();
    }

    private void initializeFragment() {
        // Initialize the Fixed Assets Fragment
        replaceFragment(new FixedAssetsFragment());
    }

    private void replaceFragment(Fragment fragment) {
        // Replaces a fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}