package com.example.uhf.mvvm.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class Item {

    @PrimaryKey(autoGenerate = false) @NonNull
    private String ecd;

    private String item;
    private String name;

    private int location;

    private double qty;

    public Item(@NonNull String ecd, String item, String name, int location, double qty) {
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

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public Double getQty() {
        return this.qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}
