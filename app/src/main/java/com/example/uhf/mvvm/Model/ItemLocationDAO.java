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
public interface ItemLocationDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemLocation location);
    @Update
    void update(ItemLocation location);
    @Delete
    void delete(ItemLocation location);
    @Query("DELETE FROM item_location")
    void deleteAllItems();
    @Query("SELECT * FROM item_location")
    LiveData<List<ItemLocation>> getAllItems();

    @Query("SELECT * FROM item_location WHERE ecd IS NULL")
    LiveData<List<ItemLocation>> getAllItemsThatAreNotRegistered();
}
