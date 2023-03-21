package com.example.uhf.mvvm.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.repository.ItemRepository;
import com.example.uhf.mvvm.Model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepository repository;
    private LiveData<List<Item>> allItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        allItems = repository.getAllItems();
    }
    public void insert(Item item) {
        repository.insert(item);
    }
    public void update (Item item) {
        repository.update(item);
    }

    public void delete (Item item) {
        repository.delete(item);
    }
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void batchInsert(Item... items) {

    }


    public void insertBatch(Context context, ArrayList<com.example.uhf.api.Item> items, int count) {
        List<Item> itemsLocations = new ArrayList<>();
        for (com.example.uhf.api.Item item: items) {
            itemsLocations.add(new Item(item.item, item.name, "", 1, item.qid));
        }
        repository.insertItemsBatch(context, count, itemsLocations.toArray(new Item[0]));
    }
}
