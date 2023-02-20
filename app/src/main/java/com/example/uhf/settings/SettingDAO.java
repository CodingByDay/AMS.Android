package com.example.uhf.settings;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.uhf.mvvm.Model.Item;

import java.util.List;

@Dao
public interface SettingDAO {
    @Insert
    void insert(Setting item);
    @Update
    void update(Setting item);
    @Delete
    void delete(Setting item);
    @Query("DELETE FROM settings")
    void deleteAllItems();
    @Query("SELECT * FROM settings")
    LiveData<List<Setting>> getAllItems();
}
