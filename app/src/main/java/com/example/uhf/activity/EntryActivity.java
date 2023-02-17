package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.uhf.R;

public class EntryActivity extends AppCompatActivity {
    private CardView btTransferLocations;
    private CardView btTransferItems;
    private CardView btListingLocations;
    private CardView btListingItems;
    private CardView btExportListing;
    private CardView btTransferListing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        getSupportActionBar().hide();

        initializeViews();
    }

    private void initializeViews() {
        btTransferLocations = findViewById(R.id.transferLocations);
        btTransferItems = findViewById(R.id.transferItems);
        btListingLocations = findViewById(R.id.listingLocations);
        btListingItems = findViewById(R.id.listingItems);
        btExportListing = findViewById(R.id.exportListing);
        btTransferListing = findViewById(R.id.transferListing);
        btTransferLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), InventoryActivity.class);
                startActivity(myIntent);
            }
        });
        btTransferItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(),  UHFMainActivity.class);
                startActivity(myIntent);
            }
        });
        btListingLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btListingItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btExportListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btTransferListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}