package com.example.uhf.mvvm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.repository.ItemRepository;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.repository.LocationRepository;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository repository;
    private LiveData<List<Location>> allItems;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        repository = new LocationRepository(application);
        allItems = repository.getAllItems();
    }
    public void insert(Location item) {
        repository.insert(item);
    }

    public void update (Location item) {
        repository.update(item);
    }

    public void delete (Location item) {
        repository.delete(item);
    }

    public LiveData<List<Location>> getAllItems() {
        return allItems;
    }

}
