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
public interface ItemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Item item);
    @Update
    void update(Item item);
    @Delete
    void delete(Item item);
    @Query("DELETE FROM item")
    void deleteAllItems();
    @Query("SELECT * FROM item")
    LiveData<List<Item>> getAllItems();
}
