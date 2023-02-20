package com.example.uhf.activity;

import android.app.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uhf.R;

public class LoginActivityMain extends AppCompatActivity {
private Button login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        getSupportActionBar().show();

        initPageViews();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        supportInvalidateOptionsMenu();
        invalidateOptionsMenu();
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initPageViews() {
        login = (Button) findViewById(R.id.login);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivityMain.this, "Prijava!", Toast.LENGTH_SHORT).show();



                Intent myIntent = new Intent(getApplicationContext(), EntryInitialActivity.class);
                //myIntent.putExtra("fragment", "entry_menu");
                startActivity(myIntent);


            }
        });
    }

}