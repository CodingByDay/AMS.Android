package com.example.uhf.mvvm.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;



@Entity(tableName = "item_location", indices = {@Index(value = {"qid"}, unique = true)})
public class ItemLocation {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String item;
    @ColumnInfo( defaultValue = "empty")

    private String code;
    @ColumnInfo(defaultValue = "empty")

    private String location;
    @ColumnInfo(defaultValue = "empty")

    private String ecd;
    @ColumnInfo(defaultValue = "empty")

    private String name;
    @ColumnInfo(defaultValue = "empty")
    private String timestamp;
    @ColumnInfo(defaultValue = "empty")

    private String user;
    @ColumnInfo(name = "qid")
    private int qid;

    public ItemLocation(String item, String code, String location, String ecd, String name, String timestamp, String user, int qid) {
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
