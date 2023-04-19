package com.example.uhf.mvvm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uhf.mvvm.Model.Asset;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.repository.AssetRepository;
import com.example.uhf.repository.CheckOutRepository;

import java.util.List;

public class CheckOutViewModel extends AndroidViewModel {
    public CheckOutViewModel(@NonNull Application application) {
        super(application);
        repository = new CheckOutRepository(application);
        allItems = repository.getAllItems();
    }


    private final CheckOutRepository repository;
    private final LiveData<List<CheckOut>> allItems;


    public void insert(CheckOut item) {
        repository.insert(item);
    }
    public void update (CheckOut item) {
        repository.update(item);
    }

    public void delete (CheckOut item) {
        repository.delete(item);
    }
    public LiveData<List<CheckOut>> getAllItems() {
        return allItems;
    }
}
