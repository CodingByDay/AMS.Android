package com.example.uhf.mvvm.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemLocationCacheDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemLocationCache location);
    @Update
    void update(ItemLocationCache location);
    @Delete
    void delete(ItemLocationCache location);
    @Query("DELETE FROM item_location_cache")
    void deleteAllItems();
    @Query("SELECT * FROM item_location_cache")
    LiveData<List<ItemLocationCache>> getAllItems();


}
