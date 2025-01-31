package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.uhf.R;

import java.util.Objects;

public class MainFragmentHolderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment_holder);

        // For now hide the action bar to get more space.
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intent = getIntent();
        String fragment = intent.getStringExtra("fragment");
        initializeFragment(fragment);
    }

    private void initializeFragment(String fragment) {
        switch (fragment) {
            case "entry_menu":
                //replaceFragment(new Entry);
                break;
        }
    }
    private void replaceFragment(Fragment fragment) {
         // Replaces a fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}