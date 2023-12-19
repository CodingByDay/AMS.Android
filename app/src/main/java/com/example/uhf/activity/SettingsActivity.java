package com.example.uhf.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.microsoft.appcenter.crashes.Crashes;

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

    private SwitchMaterial switchChecking;
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
        switchChecking = (SwitchMaterial) findViewById(R.id.extraChecking);
        switchChecking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();


                if(isChecked) {
                    editor.putString("checking", "1");
                } else {
                    editor.putString("checking", "0");
                }


                editor.apply();

            }
        });
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
                                        BaseApplicationClass baseApp = (BaseApplicationClass) getApplication();
                                        baseApp.clearAllRegisteredItems();

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
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("url", tbUrl.getText().toString());
                    editor.putString("company", tbCompany.getText().toString());
                    editor.apply();
                    settingsView.insert(new Setting("url", tbUrl.getText().toString()));
                    settingsView.insert(new Setting("company", tbCompany.getText().toString()));
                } catch (Exception e) {
                    Crashes.trackError(e);
                } finally {
                    Intent myIntent = new Intent(getApplicationContext(), LoginActivityMain.class);
                    startActivity(myIntent);
                }
            }
        });
    }
    private void updateUI(List<Setting> settings) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String checking = sharedPreferences.getString("checking", "");
        if(checking.equals("")) {
            editor.putString("checking", "1");
            editor.apply();
        }
        String check = sharedPreferences.getString("checking", "");
        if(check.equals("1")) {
            switchChecking.setChecked(true);
        } else {
            switchChecking.setChecked(false);
        }
        String url = sharedPreferences.getString("url", "");
        String company = sharedPreferences.getString("company", "");
        tbUrl.setText(url);
        tbCompany.setText(company);
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