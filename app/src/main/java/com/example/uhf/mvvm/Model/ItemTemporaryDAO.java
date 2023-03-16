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
public interface ItemTemporaryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemTemporary item);
    @Update
    void update(ItemTemporary item);
    @Delete
    void delete(ItemTemporary item);
    @Query("DELETE FROM item_temp")
    void deleteAllItems();
    @Query("SELECT * FROM item_temp")
    LiveData<List<ItemTemporary>> getAllItems();


    @Query("SELECT * FROM item_temp")
    List<ItemTemporary> getAllItemsList();





}
