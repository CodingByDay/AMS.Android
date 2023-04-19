package com.example.uhf.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.uhf.mvvm.Model.Asset;
import com.example.uhf.mvvm.Model.AssetDAO;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.CheckOutDAO;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemDAO;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationCache;
import com.example.uhf.mvvm.Model.ItemLocationCacheDAO;
import com.example.uhf.mvvm.Model.ItemLocationDAO;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.Model.ItemTemporaryDAO;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.Model.LocationDAO;
import com.example.uhf.settings.Setting;
import com.example.uhf.settings.SettingDAO;

@androidx.room.Database(entities = {Item.class, ItemTemporary.class, Setting.class, Location.class, ItemLocation.class, ItemLocationCache.class, Asset.class, CheckOut.class}, version = 17)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract ItemDAO itemDAO();
    public abstract ItemTemporaryDAO itemTemporaryDAO();

    public abstract LocationDAO locationDAO();

    public abstract ItemLocationDAO itemlocationDAO();

    public abstract ItemLocationCacheDAO itemLocationCacheDAO();

    public abstract SettingDAO settingDAO();


    public abstract AssetDAO assetDAO();

    public abstract CheckOutDAO checkOutDAO();

    public static synchronized Database getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "database")
                    .fallbackToDestructiveMigration()
                    .addCallback(databaseCallback)
                    .build();
        }
        return instance;
    }


    private static RoomDatabase.Callback databaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {


        private ItemDAO itemDAO;
        private ItemTemporaryDAO itemTemporaryDAO;
        private LocationDAO locationDAO;
        private SettingDAO  settingDAO;

        private AssetDAO assetDAO;
        private ItemLocationCacheDAO itemLocationCacheDAO;

        private CheckOutDAO checkOutDAO;
        private PopulateDbAsyncTask(Database db) {
            itemDAO = db.itemDAO();
            itemTemporaryDAO = db.itemTemporaryDAO();
            settingDAO = db.settingDAO();
            locationDAO = db.locationDAO();
            itemLocationCacheDAO = db.itemLocationCacheDAO();
            assetDAO = db.assetDAO();
            checkOutDAO = db.checkOutDAO();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            // TODO Beginning data management
            return null;
        }
    }
}
