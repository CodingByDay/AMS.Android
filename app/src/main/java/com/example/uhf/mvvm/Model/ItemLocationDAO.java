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

    @Query(" select id, item, code, location, ecd from item_location where ecd = '' and \n" +
            "item != ''\n" +
            "union\n" +
            "select a.id,'' as item,  '' as code, '' as location, '' as ecd\n" +
            "from item a\n" +
            "left join item_location b on a.item = b.item\n" +
            "group by a.item, a.qty\n" +
            "having a.qty > count(b.item)")
    LiveData<List<ItemLocation>> getAllItemsThatAreNotRegistered();



    @Query("update item_location set ecd = :edc where ID = :id ")
    void updateByID(int id, String edc);


    @Query("select id, item, code, location, ecd from item_location where ecd != ''")
    LiveData<List<ItemLocation>> getAllItemsThatAreRegistered();
}
