package com.example.uhf.mvvm.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.uhf.converter.TimeStampConverter;

import java.time.LocalDateTime;

@Entity(tableName = "item_temp")
public class ItemTemporary {


    @PrimaryKey(autoGenerate = true)
    private int ID;


    private String item;
    private String name;
    private String code;
    private String ecd;
    private String location;
    private double qty;
    private String timestamp;
    private String user;

    private String rssi;

    public ItemTemporary(String item, String name, String code, String ecd, String location, double qty, String timestamp, String user, String rssi) {
        this.item = item;
        this.name = name;
        this.code = code;
        this.ecd = ecd;
        this.location = location;
        this.qty = qty;
        this.timestamp = timestamp;
        this.user = user;
        this.rssi = rssi;
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

    public String getEcd() {
        return ecd;
    }

    public void setEcd(String ecd) {
        this.ecd = ecd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }
}
