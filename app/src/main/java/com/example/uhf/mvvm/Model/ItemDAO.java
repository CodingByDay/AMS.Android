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
    @Query("UPDATE item SET item = :item, name = :name, code = :code, qty = :qty")
    void update(String item, String name, String code, double qty);
    @Delete
    void delete(Item item);
    @Query("DELETE FROM item")
    void deleteAllItems();
    @Query("SELECT * FROM item")
    LiveData<List<Item>> getAllItems();

    @Query("SELECT * FROM item WHERE id = :id")
    Item getItemById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Item... items);
}
