package com.example.uhf.mvvm.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class Item {

    @PrimaryKey(autoGenerate = false)
    private String ecd;

    private String ident;

    private String name;

    private int location;

    public Item(String ecd, String ident, String name, int location) {
        this.ecd = ecd;
        this.ident = ident;
        this.name = name;
        this.location = location;
    }

    public void setEcd(String ecd) {
        this.ecd = ecd;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getEcd() {
        return ecd;
    }

    public String getIdent() {
        return ident;
    }

    public String getName() {
        return name;
    }

    public int getLocation() {
        return location;
    }
}
