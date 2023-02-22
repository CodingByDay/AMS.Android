package com.example.uhf.mvvm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.uhf.settings.Setting;
import com.example.uhf.settings.SettingsRepository;

import java.util.List;

public class SettingsViewModel extends AndroidViewModel {
    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = new SettingsRepository(application);
        allItems = repository.getAllItems();
    }

    private SettingsRepository repository;
    private LiveData<List<Setting>> allItems;


    public void insert(Setting item) {
        repository.insert(item);
    }



    public void update (Setting item) {
        repository.update(item);
    }

    public void delete (Setting item) {
        repository.delete(item);
    }

    public LiveData<List<Setting>> getAllItems() {
        return allItems;
    }

}
