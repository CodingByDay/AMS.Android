package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uhf.R;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;
import com.example.uhf.settings.SettingsHelper;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
private SettingsViewModel settingsView;
private List<Setting> settingsClass  = new ArrayList<Setting>();
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