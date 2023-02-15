package com.example.uhf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.CarrierConfigManager;
import android.view.View;

public class EntryInitial extends AppCompatActivity {
private CardView btInventory;
private CardView btListing;
private CardView btSync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_initial);

        getSupportActionBar().hide();


        btInventory = (CardView) findViewById(R.id.btInventory);
        btListing = (CardView) findViewById(R.id.btListing);
        btSync = (CardView) findViewById(R.id.btSync);
        btInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), Entry.class);
                startActivity(myIntent);
            }
        });
    }
}