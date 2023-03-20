package com.example.uhf.mvvm.Model;
import static androidx.room.OnConflictStrategy.REPLACE;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
@Dao
public interface AssetDAO {

    @Insert(entity = Asset.class, onConflict = REPLACE)
    void insert(Asset asset);
    @Update
    void update(Asset asset);
    @Delete
    void delete(Asset asset);
    @Query("DELETE FROM asset")
    void deleteAllItems();
    @Query("SELECT * FROM asset")
    LiveData<List<Asset>> getAllItems();


}
