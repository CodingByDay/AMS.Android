package com.example.uhf.mvvm.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "item_location_cache")
public class ItemLocationCache {

    @PrimaryKey(autoGenerate = false)
    private int ID;
    private String item;
    private String code;
    private String location;
    private String ecd;
    private String name;
    private String timestamp;
    private String user;

    private int qid;


    public ItemLocationCache(int ID, String item, String code, String location, String ecd, String name, String timestamp, String user, int qid) {
        this.ID = ID;
        this.item = item;
        this.code = code;
        this.location = location;
        this.ecd = ecd;
        this.name = name;
        this.timestamp = timestamp;
        this.user = user;
        this.qid = qid;
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

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }
}
