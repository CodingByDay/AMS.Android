package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uhf.R;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.fragment.ListingLocationsFragment;

public class ListingLocationsActivity extends AppCompatActivity {
private SearchView swListing;
private Button btExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_locations);
        getSupportActionBar().hide();
        new Thread(new Runnable() {

            @Override
            public void run() {
                ListingLocationsActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        swListing = findViewById(R.id.swListing);
                        swListing.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                ListingLocationsFragment fragment = ListingLocationsFragment.getInstance();
                                String currentColumnSearch = fragment.currentSearchColumn;
                                if(currentColumnSearch.equals("")) {
                                    currentColumnSearch = fragment.first.getText().toString();
                                }
                                fragment.adapter.searchByField(currentColumnSearch, newText);



                                return false;
                            }
                        });
                        btExit = findViewById(R.id.btExit);
                        btExit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent myIntent = new Intent(getApplicationContext(), EntryInitialActivity.class);
                                startActivity(myIntent);
                                finishAffinity();
                            }
                        });

                        replaceFragment();
                    }
                });
            }
        }).start();

    }

    private void replaceFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new ListingLocationsFragment());
        fragmentTransaction.commit();
    }
}