package com.example.uhf.mvvm.Model;

import static androidx.room.OnConflictStrategy.REPLACE;

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
    @Insert(entity = ItemLocation.class, onConflict = REPLACE)
    void insert(ItemLocation location);
    @Update
    void update(ItemLocation location);
    @Delete
    void delete(ItemLocation location);
    @Query("DELETE FROM item_location")
    void deleteAllItems();
    @Query("SELECT * FROM item_location")
    LiveData<List<ItemLocation>> getAllItems();

    @Query("select id, item, name, code, location, ecd from item_location" +
            " where ecd = '' and item != '' union select a.id,'' as item,'' as name, '' as code, '' as location, '' as ecd from item a left join\n" +
            "item_location b on a.item = b.item group by a.item, a.qty having a.qty > count(b.item)")
    LiveData<List<ItemLocation>> getAllItemsThatAreNotRegistered();



    @Query("update item_location set ecd = :edc where ID = :id ")
    void updateByID(int id, String edc);


    @Query("select id, item, code, location, ecd from item_location where ecd != ''")
    LiveData<List<ItemLocation>> getAllItemsThatAreRegistered();



    @Query ("select * from item_location where ecd is :ecd")
    ItemLocation getItemByEcd(String ecd);
}
