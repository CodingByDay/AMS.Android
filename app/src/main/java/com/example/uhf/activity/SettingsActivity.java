package com.example.uhf.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.uhf.R;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
private SettingsViewModel settingsView;
private List<Setting> settingsClass  = new ArrayList<Setting>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initActivity();
    }

    private void initActivity() {
        settingsView = ViewModelProviders.of((SettingsActivity) this).get(SettingsViewModel.class);
        /*settingsView.getAllItems().observe(SettingsActivity.this.cp, new Observer<List<Setting>>() {
            @Override
            public void onChanged(List<Setting> settings) {
                settings = settings;
            }
        });*/
    }
}