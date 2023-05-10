package com.example.uhf.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uhf.R;
import com.example.uhf.barcode.BarcodeUtility;
import com.example.uhf.database.Database;
import com.example.uhf.fragment.FixedAssetsFragment;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;
import com.example.uhf.settings.SettingsHelper;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
private SettingsViewModel settingsView;
private List<Setting> settingsClass  = new ArrayList<Setting>();
private ImageView delete;
private TextView btSave;
    private EditText tbUrl;
    private SettingsHelper settingsHelper = new SettingsHelper();
    private EditText tbCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        initActivity();
        initUI();
    }

    private void initUI() {
        btSave = (TextView) findViewById(R.id.btSave);
        tbUrl = (EditText) findViewById(R.id.tbUrl);
        tbCompany = (EditText) findViewById(R.id.tbCompany);
        delete = (ImageView) findViewById(R.id.deleteDatabase);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
                alert.setTitle("Brisanje podatkov");
                alert.setMessage("Ali ste sigurni da želite pobrisati podatke?");
                alert.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog mypDialog = new ProgressDialog(SettingsActivity.this);
                        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        mypDialog.setMessage("Brisanje podatkov...");
                        mypDialog.setCanceledOnTouchOutside(false);
                        mypDialog.show();
                        new Thread(new Runnable() {




                                    @Override
                                    public void run() {
                                        Database db = Database.getInstance(SettingsActivity.this);
                                        db.clearAllTables();

                                    }


                        }).start();
                        mypDialog.cancel();
                       Toast.makeText(SettingsActivity.this, "Podatki pobrisani", Toast.LENGTH_SHORT).show();
                       dialog.dismiss();
                    }
                });
                alert.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();

            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    settingsView.insert(new Setting("url", tbUrl.getText().toString()));
                    settingsView.insert(new Setting("company", tbCompany.getText().toString()));
                } catch (Exception e) {
                    // TODO application wide logging
                } finally {
                    Intent myIntent = new Intent(getApplicationContext(), LoginActivityMain.class);
                    startActivity(myIntent);
                }
            }
        });
    }
    private void updateUI(List<Setting> settings) {
        tbUrl.setText(SettingsHelper.Helper.findSetting(settings, "url").getValue());
        tbCompany.setText(SettingsHelper.Helper.findSetting(settings, "company").getValue());
    }
    private void initActivity() {
        settingsView = ViewModelProviders.of(this).get(SettingsViewModel.class);
        settingsView.getAllItems().observe(this, new Observer<List<Setting>>() {
            @Override
            public void onChanged(List<Setting> settings) {
                updateUI(settings);
            }

        });
    }
}