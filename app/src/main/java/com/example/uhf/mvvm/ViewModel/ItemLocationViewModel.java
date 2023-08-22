package com.example.uhf.mvvm.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uhf.api.Asset;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.repository.ItemLocationRepository;
import com.example.uhf.tools.SettingsHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ItemLocationViewModel extends AndroidViewModel {
    private final LiveData<List<ItemLocation>> allItemsQuery;
    private final LiveData<List<ItemLocation>> allItemsRegistered;
    private ItemLocationRepository repository;
    private LiveData<List<ItemLocation>> allItems;
    public ItemLocationViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemLocationRepository(application);
        allItems = repository.getAllItems();
        allItemsQuery = repository.getAllItemsNotRegistered();
        allItemsRegistered = repository.getAllItemsRegistered();
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
        return repository.getAllItemsNotRegistered();
    }

    public void updateEPCByID(int id, String epc) {
        repository.updateById(id, epc);
    }

    public LiveData<List<ItemLocation>>getItemsThatAreRegistered() {
        return allItemsRegistered;
    }


    public void insertBatch(Context context, ArrayList<Asset> items, int count) {
        List<com.example.uhf.mvvm.Model.ItemLocation> itemsLocations = new ArrayList<>();
        for (com.example.uhf.api.Asset item: items) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if(item.name == null ) {
                item.name = "";
            }
            itemsLocations.add(new com.example.uhf.mvvm.Model.ItemLocation(item.item, item.code, item.location, item.ecd, item.name, timestamp.toString(), "", item.qid,  item.careTaker));
        }
        repository.insertItemsBatch(context, count, itemsLocations.toArray(new com.example.uhf.mvvm.Model.ItemLocation[0]));
    }
}
