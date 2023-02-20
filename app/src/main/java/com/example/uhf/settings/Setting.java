package com.example.uhf.settings;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings")
public class Setting {
    @PrimaryKey(autoGenerate = false)
    private String key;
    private String value;

    public Setting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Setting() {
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
