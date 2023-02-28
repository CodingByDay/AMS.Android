package com.example.uhf.database;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.uhf.api.Asset;
import com.example.uhf.api.AsyncCallBack;
import com.example.uhf.api.Item;
import com.example.uhf.api.Location;
import com.example.uhf.api.Root;
import com.example.uhf.api.RootAsset;
import com.example.uhf.api.RootLocation;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.mvvm.ViewModel.LocationViewModel;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;

public class ImportExportData {

    private ItemViewModel itemViewModel;
    private ItemLocationViewModel itemLocationViewModel;
    private LocationViewModel locationViewModel;

    private AsyncCallBack callBack;


    public ImportExportData(FragmentActivity context) {
        itemViewModel = ViewModelProviders.of(context).get(ItemViewModel.class);
        itemLocationViewModel = ViewModelProviders.of(context).get(ItemLocationViewModel.class);
        locationViewModel = ViewModelProviders.of(context).get(LocationViewModel.class);
        callBack = (AsyncCallBack) context;
        boolean debug = true;
    }

    public void commitToLocalStorage(Root rootItems, RootLocation rootLocations, RootAsset rootAssets) {
        for (Item item: rootItems.items) {
            itemViewModel.insert(new  com.example.uhf.mvvm.Model.Item(item.item, item.name, "", 1));
        }
        callBack.setProgressValue(64);
        // update progress
        for(Location location:rootLocations.locations ) {
            locationViewModel.insert(new com.example.uhf.mvvm.Model.Location("","", ""));
        }
        callBack.setProgressValue(80);

        // update progress
        for(int i = 0; i < 10000;i++)  {
            itemLocationViewModel.insert(new com.example.uhf.mvvm.Model.ItemLocation("test", "", "", ""));
        }
        callBack.setProgressValue(100);

    }

}
