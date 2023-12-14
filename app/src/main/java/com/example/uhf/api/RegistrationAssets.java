package com.example.uhf.api;

import com.example.uhf.mvvm.Model.ItemLocation;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationAssets {
    private String token;
    private List<AssetRegistration> assets;
    public RegistrationAssets(String token) {
        this.token = token;
        this.assets = new ArrayList<>();
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

    public void addAssets(List<ItemLocation> items) {
        for (ItemLocation item : items)
        {
            this.assets.add(new AssetRegistration(item.getID(), "", item.getItem(), item.getLocation(), item.getEcd(), item.getName(), 3, 3));
        }
    }


    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }



}
