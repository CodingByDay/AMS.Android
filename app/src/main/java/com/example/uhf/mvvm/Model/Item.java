package com.example.uhf.mvvm.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "item", indices = {@Index(value = {"qid"}, unique = true)})
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    @ColumnInfo(defaultValue = "")
    private String item;
    @ColumnInfo(defaultValue = "")
    private String name;
    @ColumnInfo(defaultValue = "")
    private String code;
    private double qty;


    @ColumnInfo(name = "qid")
    private int qid;

    public Item(String item, String name, String code, double qty, int qid) {
        this.item = item;
        this.name = name;
        this.code = code;
        this.qty = qty;
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

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }
}
