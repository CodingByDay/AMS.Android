package com.example.uhf.mvvm.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "item_location")
public class ItemLocation {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String item;
    private String code;
    private String location;
    private String ecd;
}
