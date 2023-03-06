package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uhf.R;

public class EntryInitialActivity extends AppCompatActivity {
private CardView btRegistration;
private CardView btInventory;
private CardView btListing;
private CardView btSync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_initial);
        getSupportActionBar().hide();
        btRegistration = (CardView) findViewById(R.id.btRegistration);
        btInventory = (CardView) findViewById(R.id.btInventory);
        btListing = (CardView) findViewById(R.id.btListing);
        btSync = (CardView) findViewById(R.id.btSync);
        btRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(myIntent);
            }
        });
        btInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), ListingActivity.class);
                startActivity(myIntent);
            }
        });
        btSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), EntryActivity.class);
                startActivity(myIntent);
            }
        });
        Toast.makeText(this, "Uspešna prijava.", Toast.LENGTH_SHORT).show();
    }
}