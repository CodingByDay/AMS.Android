package com.example.uhf.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemDAO;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationDAO;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.Model.ItemTemporaryDAO;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.Model.LocationDAO;
import com.example.uhf.settings.Setting;
import com.example.uhf.settings.SettingDAO;

@androidx.room.Database(entities = {Item.class, ItemTemporary.class, Setting.class, Location.class, ItemLocation.class}, version = 7)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract ItemDAO itemDAO();
    public abstract ItemTemporaryDAO itemTemporaryDAO();

    public abstract LocationDAO locationDAO();

    public abstract ItemLocationDAO itemlocationDAO();

    public abstract SettingDAO settingDAO();

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
        private PopulateDbAsyncTask(Database db) {
            itemDAO = db.itemDAO();
            itemTemporaryDAO = db.itemTemporaryDAO();
            settingDAO = db.settingDAO();
            locationDAO = db.locationDAO();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            itemDAO.insert(new Item("I12537","Novi motor","25",3));
            itemDAO.insert(new Item("I18263","Industrijska stiskalnica","5", 5));
            itemDAO.insert(new Item("I91837","Učinkovita plazemska reza","", 5));
            itemDAO.insert(new Item("I17231","Preizkušeni generator za električno energijo","5", 5));
            itemDAO.insert(new Item("I16732","Kompaktni laserski gravirnik","256", 5));
            itemDAO.insert(new Item("I01273","Visokokakovostna vijačna matrica","265", 5));
            itemDAO.insert(new Item("I01273","Digitalna tiskalna naprava","235", 5));
            itemDAO.insert(new Item("I71123","Zmogljiv kompresor zraka","25", 5));
            itemDAO.insert(new Item("I98162","Robustna kroglična obdelovalna masina","5", 5));
            itemDAO.insert(new Item("I17892","Profesionalna stacionarna sesalna naprava","5", 5));
            itemDAO.insert(new Item("I97123","Napredna stiskalnica za plastiko","", 5));
            itemDAO.insert(new Item("I00912","Nakladalna tehnika za materiale","253", 5));
            itemDAO.insert(new Item("I07112","Procesna oprema za kemične procese","25", 5));
            itemDAO.insert(new Item("I98163","Učinkovit kotlični pritisk","225", 5));
            itemDAO.insert(new Item("I89172","Zmogljivi rezalni stroj","", 5));

            locationDAO.insert(new Location("001", "Obrat 2", ""));
            locationDAO.insert(new Location("002", "Obrat 1", ""));
            locationDAO.insert(new Location("003", "Servis", ""));
            locationDAO.insert(new Location("004", "Kooperacija", ""));


            return null;
        }
    }
}
