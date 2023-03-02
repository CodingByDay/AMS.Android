package com.example.uhf.mvvm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationCache;
import com.example.uhf.repository.ItemLocationCacheRepository;
import com.example.uhf.repository.ItemLocationRepository;

import java.util.List;

public class ItemLocationCacheViewModel extends AndroidViewModel {
    private final LiveData<List<ItemLocationCache>> allItemsQuery;
    private ItemLocationCacheRepository repository;
    private LiveData<List<ItemLocationCache>> allItems;
    public ItemLocationCacheViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemLocationCacheRepository(application);
        allItems = repository.getAllItems();
        allItemsQuery = repository.getAllItemsNotRegistered();
    }
    public void insert(ItemLocationCache item) {
        repository.insert(item);
    }
    public void update (ItemLocationCache item) {
        repository.update(item);
    }
    public void delete (ItemLocationCache item) {
        repository.delete(item);
    }
    public LiveData<List<ItemLocationCache>> getAllItems() {
        return allItems;
    }
    public LiveData<List<ItemLocationCache>> getAllItemsNotRegistered() {
        return allItemsQuery;
    }
}
