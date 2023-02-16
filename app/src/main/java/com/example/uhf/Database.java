package com.example.uhf;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemDAO;

@androidx.room.Database(entities = {Item.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract ItemDAO itemDAO();

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
        private PopulateDbAsyncTask(Database db) {
            itemDAO = db.itemDAO();
        }


        @Override
        protected Void doInBackground(Void... voids) {
//            itemDAO.insert(new Item("E280116060000213B8204D0F",",ID63230",",Novi motor",25));
//            itemDAO.insert(new Item("E28011606000021185EFE55D",",ID48139",",Industrijska stiskalnica",46));
//            itemDAO.insert(new Item("E28011606000021185EFD24C",",ID81392",",Učinkovita plazemska reza",67));
//            itemDAO.insert(new Item("E280116060000213B81FD548",",ID99076",",Preizkušeni generator za električno energijo",30));
//            itemDAO.insert(new Item("E280116060000213B8201128",",ID82256",",Kompaktni laserski gravirnik",89));
//            itemDAO.insert(new Item("E280116060000213B8209B45",",ID48002",",Visokokakovostna vijačna matrica",39));
//            itemDAO.insert(new Item("E280116060000213B820349E",",ID69811",",Digitalna tiskalna naprava",20));
//            itemDAO.insert(new Item("E280116060000213B81FA1CD",",ID75467",",Zmogljiv kompresor zraka",65));
//            itemDAO.insert(new Item("E280116060000213B8204D28",",ID75677",",Robustna kroglična obdelovalna masina",8));
//            itemDAO.insert(new Item("E280116060000213B81F43FA",",ID18472",",Profesionalna stacionarna sesalna naprava",6));
//            itemDAO.insert(new Item("E280116060000213B81F3155",",ID23530",",Napredna stiskalnica za plastiko",81));
//            itemDAO.insert(new Item("E280116060000213B820B518",",ID59175",",Nakladalna tehnika za materiale",77));
//            itemDAO.insert(new Item("E28011606000021185EFA96A",",ID95494",",Procesna oprema za kemične procese",30));
//            itemDAO.insert(new Item("E28011606000021185EF4B5F",",ID76224",", Učinkovit kotlični pritisk",43));
//            itemDAO.insert(new Item("E280116060000213B81F543C",",ID86653",", Zmogljivi rezalni stroj",18));
//            itemDAO.insert(new Item("E28011606000021185EF4B9D",",ID84966",",Avtomatizirani transportni sistemi",95));
//            itemDAO.insert(new Item("E280116060000213B820AC10",",ID53827",",Merilne naprave za natančno mjerenje",10));
//            itemDAO.insert(new Item("E280116060000213B820B53D",",ID91839",",Avtomatsko testiranje elektronske opreme",62));
//            itemDAO.insert(new Item("E280116060000213B8202B46",",ID89259",",CNC obdelovalni stroji",87));
//            itemDAO.insert(new Item("E28011606000021185F021AC",",ID74007",",Profesionalne stiskalnice za metal",9));
//            itemDAO.insert(new Item("E280116060000213B81FD5BB",",ID63491",",Zmogljive pnevmatske vijačne matrice",69));
//            itemDAO.insert(new Item("E28011606000021185F01054",",ID91561",",Avtomatska linija za proizvodnjo kovin",15));
//            itemDAO.insert(new Item("E28011606000021185EF3080",",ID99494",",Procesna oprema za proizvodnjo polimerov",53));
//            itemDAO.insert(new Item("E280116060000213B81FCCF1",",ID55637",",Oprema za sušenje materialov",93));
//            itemDAO.insert(new Item("E280116060000213B81F43F4",",ID56886",",Visokotlačne črpalke za tekočine",76));
//            itemDAO.insert(new Item("E28011606000021185EFC3CD",",ID86410",",Profesionalne brizgalne naprave",75));
//            itemDAO.insert(new Item("E28011606000021185EF6D7F",",ID45205",",Oprema za mehansko obdelavo kovin",70));
//            itemDAO.insert(new Item("E28011606000021185EF64EA",",ID70499",",Avtomatski sistemi za valjanje kovin",78));
//            itemDAO.insert(new Item("E280116060000213B81FA150",",ID7620",",Zmogljive frekvenčne inverterje",12));
            return null;
        }
    }
}
