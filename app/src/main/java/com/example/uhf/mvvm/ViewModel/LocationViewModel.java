package com.example.uhf.mvvm.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.repository.LocationRepository;

import java.util.ArrayList;
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

    public void insertBatch(Context context, ArrayList<com.example.uhf.api.Location> items) {
        List<Location> itemsLocations = new ArrayList<>();
        for (com.example.uhf.api.Location loc: items) {

            itemsLocations.add(new Location(loc.location, loc.name, loc.code, loc.dept));
        }

        repository.insertLocationsBatch(context, itemsLocations.toArray(new Location[0]));
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
