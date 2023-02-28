package com.example.uhf.mvvm.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String item;
    private String name;
    private String code;

    private double qty;

    public Item(String item, String name, String code, double qty) {
        this.item = item;
        this.name = name;
        this.code = code;
        this.qty = qty;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}
