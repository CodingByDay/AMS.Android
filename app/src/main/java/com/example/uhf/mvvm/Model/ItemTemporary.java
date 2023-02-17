package com.example.uhf.mvvm.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_temp")
public class ItemTemporary {

    @PrimaryKey(autoGenerate = false) @NonNull
    private String ecd;

    private String item;
    private String name;

    private String location;


    private double qty;

    public ItemTemporary(@NonNull String ecd, String item, String name, String location, double qty) {
        this.ecd = ecd;
        this.item = item;
        this.name = name;
        this.location = location;
        this.qty = qty;
    }


    @NonNull
    public String getEcd() {
        return ecd;
    }

    public void setEcd(@NonNull String ecd) {
        this.ecd = ecd;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getQty() {
        return this.qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}
