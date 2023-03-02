package com.example.uhf.mvvm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.repository.ItemLocationRepository;
import com.example.uhf.repository.LocationRepository;

import java.util.List;

public class ItemLocationViewModel extends AndroidViewModel {
    private final LiveData<List<ItemLocation>> allItemsQuery;
    private ItemLocationRepository repository;
    private LiveData<List<ItemLocation>> allItems;
    public ItemLocationViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemLocationRepository(application);
        allItems = repository.getAllItems();
        allItemsQuery = repository.getAllItemsNotRegistered();
    }
    public void insert(ItemLocation item) {
        repository.insert(item);
    }
    public void update (ItemLocation item) {
        repository.update(item);
    }
    public void delete (ItemLocation item) {
        repository.delete(item);
    }
    public LiveData<List<ItemLocation>> getAllItems() {
        return allItems;
    }
    public LiveData<List<ItemLocation>> getAllItemsNotRegistered() {
        return allItemsQuery;
    }

    public void updateEPCByID(int id, String epc) {
        repository.updateById(id, epc);
    }

}
