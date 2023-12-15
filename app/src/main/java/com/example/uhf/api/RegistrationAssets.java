package com.example.uhf.api;

import com.example.uhf.activity.BaseApplicationClass;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.microsoft.appcenter.crashes.Crashes;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class RegistrationAssets  {
    private String token;
    private List<AssetRegistration> assets;
    public RegistrationAssets(String token) {
        this.token = token;
        this.assets = new ArrayList<>();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<AssetRegistration> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetRegistration> assets) {
        this.assets = assets;
    }


    public static class AssetRegistration {


        public AssetRegistration(int QID, String type, String item, String location, String ECD, String name, int userChg, int userIns) {
            this.QID = QID;
            Type = type;
            Item = item;
            Location = location;
            this.ECD = ECD;
            Name = name;
            UserChg = userChg;
            UserIns = userIns;
        }
        private int QID;
        private String Type;
        private String Item;
        private String Location;
        private String ECD;
        private String Name;
        private int UserChg;
        private int UserIns;

    }

    public void addAssets(List<ItemLocation> items, int user) {


        for (ItemLocation item : items)
        {
            this.assets.add(new AssetRegistration(item.getID(), "", item.getItem(), item.getLocation(), item.getEcd(), item.getName(), user, user));
        }
    }


    public String toJson() {
        // Convert to JSON
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }



}
