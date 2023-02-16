package com.example.uhf.activity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uhf.R;

public class LoginActivityMain extends Activity {
private Button login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        initPageViews();



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