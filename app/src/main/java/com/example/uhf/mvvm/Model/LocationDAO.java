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
public interface LocationDAO {
    @Insert
    void insert(Location location);
    @Query ("UPDATE location SET name = :name, code = :code, dept = :dept WHERE location = :location")
    void update(String name, String code, String dept, String location);
    @Delete
    void delete(Location location);
    @Query("DELETE FROM location")
    void deleteAllItems();
    @Query("SELECT * FROM location")
    LiveData<List<Location>> getAllItems();


}
