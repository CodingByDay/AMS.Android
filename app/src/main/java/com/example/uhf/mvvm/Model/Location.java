package com.example.uhf.mvvm.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "location")
public class Location {


    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String location;
    private String name;
    private String code;


    public Location(String location, String name, String code) {

        this.location = location;
        this.name = name;
        this.code = code;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
