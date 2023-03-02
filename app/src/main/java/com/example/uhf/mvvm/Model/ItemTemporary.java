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
    private String ecd;
    private String name;
    private String code;
    private String location;
    private double qty;
    private String timestamp;
    private String user;

    private String rssi;

    public ItemTemporary(String ecd, String name, String code, String location, double qty, String timestamp, String user, String rssi) {
        this.ecd = ecd;
        this.name = name;
        this.code = code;
        this.location = location;
        this.qty = qty;
        this.timestamp = timestamp;
        this.user = user;
        this.rssi = rssi;
    }


    @Ignore
    public ItemTemporary(String rssi) {
        this.rssi = rssi;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEcd() {
        return ecd;
    }

    public void setEcd(String ecd) {
        this.ecd = ecd;
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
