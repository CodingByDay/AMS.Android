package com.example.uhf.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemDAO;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.Model.ItemTemporaryDAO;

@androidx.room.Database(entities = {Item.class, ItemTemporary.class}, version = 4)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract ItemDAO itemDAO();
    public abstract ItemTemporaryDAO itemTemporaryDAO();

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
        private PopulateDbAsyncTask(Database db) {
            itemDAO = db.itemDAO();
            itemTemporaryDAO = db.itemTemporaryDAO();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            itemDAO.insert(new Item("E280116060000213B8204D0F","ID63230","Novi motor","25",3));
            itemDAO.insert(new Item("E28011606000021185EFE55D","ID48139","Industrijska stiskalnica","5", 5));
            itemDAO.insert(new Item("E28011606000021185EFD24C","ID81392","Učinkovita plazemska reza","", 5));
            itemDAO.insert(new Item("E280116060000213B81FD548","ID99076","Preizkušeni generator za električno energijo","5", 5));
            itemDAO.insert(new Item("E280116060000213B8201128","ID82256","Kompaktni laserski gravirnik","256", 5));
            itemDAO.insert(new Item("E280116060000213B8209B45","ID48002","Visokokakovostna vijačna matrica","265", 5));
            itemDAO.insert(new Item("E280116060000213B820349E","ID69811","Digitalna tiskalna naprava","235", 5));
            itemDAO.insert(new Item("E280116060000213B81FA1CD","ID75467","Zmogljiv kompresor zraka","25", 5));
            itemDAO.insert(new Item("E280116060000213B8204D28","ID75677","Robustna kroglična obdelovalna masina","5", 5));
            itemDAO.insert(new Item("E280116060000213B81F43FA","ID18472","Profesionalna stacionarna sesalna naprava","5", 5));
            itemDAO.insert(new Item("E280116060000213B81F3155","ID23530","Napredna stiskalnica za plastiko","", 5));
            itemDAO.insert(new Item("E280116060000213B820B518","ID59175","Nakladalna tehnika za materiale","253", 5));
            itemDAO.insert(new Item("E28011606000021185EFA96A","ID95494","Procesna oprema za kemične procese","25", 5));
            itemDAO.insert(new Item("E28011606000021185EF4B5F","ID76224","Učinkovit kotlični pritisk","225", 5));
            itemDAO.insert(new Item("E280116060000213B81F543C","ID86653","Zmogljivi rezalni stroj","", 5));
//            itemDAO.insert(new Item("E28011606000021185EF4B9D",",ID84966",",Avtomatizirani transportni sistemi",95, 5));
//            itemDAO.insert(new Item("E280116060000213B820AC10",",ID53827",",Merilne naprave za natančno mjerenje",10, 5));
//            itemDAO.insert(new Item("E280116060000213B820B53D",",ID91839",",Avtomatsko testiranje elektronske opreme",62, 5));
//            itemDAO.insert(new Item("E280116060000213B8202B46",",ID89259",",CNC obdelovalni stroji",87, 5));
//            itemDAO.insert(new Item("E28011606000021185F021AC",",ID74007",",Profesionalne stiskalnice za metal",9, 5));
//            itemDAO.insert(new Item("E280116060000213B81FD5BB",",ID63491",",Zmogljive pnevmatske vijačne matrice",69, 5));
//            itemDAO.insert(new Item("E28011606000021185F01054",",ID91561",",Avtomatska linija za proizvodnjo kovin",15, 5));
//            itemDAO.insert(new Item("E28011606000021185EF3080",",ID99494",",Procesna oprema za proizvodnjo polimerov",53, 5));
//            itemDAO.insert(new Item("E280116060000213B81FCCF1",",ID55637",",Oprema za sušenje materialov",93, 5));
//            itemDAO.insert(new Item("E280116060000213B81F43F4",",ID56886",",Visokotlačne črpalke za tekočine",76, 5));
//            itemDAO.insert(new Item("E28011606000021185EFC3CD",",ID86410",",Profesionalne brizgalne naprave",75, 5));
//            itemDAO.insert(new Item("E28011606000021185EF6D7F",",ID45205",",Oprema za mehansko obdelavo kovin",70, 5));
//            itemDAO.insert(new Item("E28011606000021185EF64EA",",ID70499",",Avtomatski sistemi za valjanje kovin",78, 5));
//            itemDAO.insert(new Item("E280116060000213B81FA150",",ID7620",",Zmogljive frekvenčne inverterje",12, 5));
            return null;
        }
    }
}
