package com.example.uhf.mvvm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uhf.item.ItemRepository;
import com.example.uhf.mvvm.Model.Item;

import java.util.List;

public class SettingsViewModel extends AndroidViewModel {
    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        allItems = repository.getAllItems();
    }

    private ItemRepository repository;
    private LiveData<List<Item>> allItems;


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

}
