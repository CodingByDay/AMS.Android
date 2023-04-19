package com.example.uhf.mvvm.Model;
import static androidx.room.OnConflictStrategy.REPLACE;
import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

public interface CheckOutDAO {
    @Insert(entity = CheckOut.class, onConflict = REPLACE)
    void insert(CheckOut asset);
    @Update
    void update(CheckOut asset);
    @Delete
    void delete(CheckOut asset);
    @Query("DELETE FROM check_out")
    void deleteAllItems();
    @Query("SELECT * FROM check_out")
    LiveData<List<CheckOut>> getAllItems();
}
