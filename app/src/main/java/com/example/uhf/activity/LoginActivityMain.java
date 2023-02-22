package com.example.uhf.activity;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.example.uhf.api.Communicator;

public class LoginActivityMain extends AppCompatActivity {
private Button login;
    private ProgressDialog mypDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        getSupportActionBar().show();

        initPageViews();



    }


    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            case R.id.UHF_ver:
                String versionName = "";
                try {
                    versionName = this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("O aplikaciji");
                builder.setMessage("Inventura verzija: " + versionName);
                builder.setPositiveButton("Zapri", ((dialogInterface, i) -> {

                }));
                builder.show();
                break;
            case R.id.sync:
                 SyncData();
                break;
            case R.id.settings:
                Toast.makeText(this, "Nastavitve", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                //myIntent.putExtra("fragment", "entry_menu");
                startActivity(myIntent);
                break;
            default:
                break;
        }
        return true;
    }

    private void SyncData() {
        boolean connected = Communicator.CommunicatorAPI.isDeviceConnected(this);
        if(!connected) {
        Toast.makeText(this, "Ni povezave!", Toast.LENGTH_SHORT).show();
         return;
        }
        mypDialog = new ProgressDialog(this);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mypDialog.setMessage("Pridobivanje podatkov...");
        mypDialog.setCanceledOnTouchOutside(false);
        mypDialog.show();
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