package com.example.uhf.mvvm.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.lidroid.xutils.db.annotation.Unique;


@Entity(tableName = "location", indices = {@Index(value = {"location", "name"}, unique = true)})
public class Location {


    @PrimaryKey(autoGenerate = true)
    private int ID;
    @ColumnInfo(name = "location")
    private String location;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "code")
    private String code;
    @ColumnInfo(name = "dept")
    private String dept;


    public Location(String location, String name, String code, String dept) {

        this.location = location;
        this.name = name;
        this.code = code;
        this.dept = dept;
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

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
