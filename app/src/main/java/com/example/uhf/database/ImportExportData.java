package com.example.uhf.database;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.uhf.mvvm.Model.LocationDAO;
import com.example.uhf.mvvm.ViewModel.ItemLocationViewModel;
import com.example.uhf.mvvm.ViewModel.ItemViewModel;
import com.example.uhf.mvvm.ViewModel.LocationViewModel;
import com.example.uhf.mvvm.ViewModel.SettingsViewModel;

import java.util.ArrayList;

public class ImportExportData {
    private ItemViewModel itemViewModel;
    private ItemLocationViewModel itemLocationViewModel;
    private LocationViewModel locationViewModel;
    private AsyncCallBack callBack;
    Context context;

    public ImportExportData(FragmentActivity context) {

        itemViewModel = ViewModelProviders.of(context).get(ItemViewModel.class);
        itemLocationViewModel = ViewModelProviders.of(context).get(ItemLocationViewModel.class);
        locationViewModel = ViewModelProviders.of(context).get(LocationViewModel.class);

        this.context = context;
        callBack = (AsyncCallBack) context;

    }
    public void commitToLocalStorageLocations(RootLocation rootLocations) {


        Context inner = this.context;
        locationViewModel.insertBatch(this.context,rootLocations.locations);

    }





    public void commitToLocalStorageMaterial(Root rootItems,  RootAsset rootAssets) {

        int count = rootItems.items.size() + rootAssets.assets.size();
        itemViewModel.insertBatch(this.context, rootItems.items, count);
        itemLocationViewModel.insertBatch(this.context, rootAssets.assets, count);


    }

}
