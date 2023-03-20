package com.example.uhf.mvvm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uhf.mvvm.Model.Asset;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.repository.AssetRepository;
import com.example.uhf.repository.ItemLocationRepository;

import java.util.List;

public class AssetViewModel extends AndroidViewModel {
    private final AssetRepository repository;
    private final LiveData<List<Asset>> allItems;

    public AssetViewModel(@NonNull Application application) {
        super(application);
        repository = new AssetRepository(application);
        allItems = repository.getAllItems();

    }
    public void insert(Asset item) {
        repository.insert(item);
    }
    public void update (Asset item) {
        repository.update(item);
    }

    public void delete (Asset item) {
        repository.delete(item);
    }
    public LiveData<List<Asset>> getAllItems() {
        return allItems;
    }


}
