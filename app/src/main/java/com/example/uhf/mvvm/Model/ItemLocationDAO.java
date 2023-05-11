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


    @Query("UPDATE item_location SET item = :item, code = :code, location = :location, ecd = :ecd, name = :name, timestamp = :timestamp WHERE qid = :qid")
    void update(String item, String code, String location, String ecd, String name, String timestamp, int qid);
    @Delete
    void delete(ItemLocation location);
    @Query("DELETE FROM item_location")
    void deleteAllItems();
    @Query("SELECT * FROM item_location")
    LiveData<List<ItemLocation>> getAllItems();

    @Query("select id, item, name, code, location, ecd, qid from item_location where ifnull(ecd,'') = ''\n" +
            "union\n" +
            "select a.id, a.item,a.name, '' as code, '' as location, '' as ecd, 0 as qid\n" +
            "from item a\n" +
            "left join item_location b on a.item = b.item\n" +
            "group by a.[item], a.[qty]\n" +
            "having a.qty > count(b.item)")
    LiveData<List<ItemLocation>> getAllItemsThatAreNotRegistered();



    @Query("update item_location set ecd = :edc where ID = :id ")
    void updateByID(int id, String edc);


    @Query("select id, item, name, code, location, ecd, qid from item_location where ecd != ''")
    LiveData<List<ItemLocation>> getAllItemsThatAreRegistered();



    @Query ("select * from item_location where ecd is :ecd")
    ItemLocation getItemByEcd(String ecd);
}
