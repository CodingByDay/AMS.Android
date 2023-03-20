package com.example.uhf.mvvm.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;





@Entity(tableName = "asset")
public class Asset {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String item;
    private String code;
    private String name;
    private String location;
    private String epc;
    private String timestamp;
    private String user;

    private boolean committed;


    public Asset(String item, String code, String name, String location, String epc, String timestamp, String user, boolean committed) {
        this.item = item;
        this.code = code;
        this.name = name;
        this.location = location;
        this.epc = epc;
        this.timestamp = timestamp;
        this.user = user;
        this.committed = committed;
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

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
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

    public boolean isCommitted() {
        return committed;
    }

    public void setCommitted(boolean committed) {
        this.committed = committed;
    }
}
