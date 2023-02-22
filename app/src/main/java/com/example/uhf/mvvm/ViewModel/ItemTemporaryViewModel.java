package com.example.uhf.mvvm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uhf.repository.ItemTemporaryRepository;
import com.example.uhf.mvvm.Model.ItemTemporary;

import java.util.List;

public class ItemTemporaryViewModel extends AndroidViewModel {

    private ItemTemporaryRepository repository;
    private LiveData<List<ItemTemporary>> allItems;

    public ItemTemporaryViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemTemporaryRepository(application);
        allItems = repository.getAllItems();
    }
    public void insert(ItemTemporary item) {
        repository.insert(item);
    }


    /**
     *
     * @param key
     * @return true if the key is already present
     */
    public boolean primaryKeyExists(List<ItemTemporary> items, String key) {
        boolean result = false;
        for (ItemTemporary item : items) {
            if(item.getEcd().equals(key)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void update (ItemTemporary item) {
        repository.update(item);
    }

    public void delete (ItemTemporary item) {
        repository.delete(item);
    }

    public LiveData<List<ItemTemporary>> getAllItems() {
        return allItems;
    }

    public void deleteAllItems() {
        repository.deleteAllItems();
    }

}
