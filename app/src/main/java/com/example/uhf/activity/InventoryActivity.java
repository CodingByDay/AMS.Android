package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uhf.R;

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);
        getSupportActionBar().hide();

    }
}