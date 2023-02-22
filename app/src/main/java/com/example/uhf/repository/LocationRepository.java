package com.example.uhf.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.uhf.database.Database;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemDAO;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.Model.LocationDAO;

import java.util.List;

public class LocationRepository {
    private LocationDAO itemDAO;
    private LiveData<List<Location>> allItems;


    public LocationRepository(Application application) {
        Database database = Database.getInstance(application);
        itemDAO = database.locationDAO();
        allItems = itemDAO.getAllItems();
    }

    public void insert(Location item) {
        new LocationRepository.InsertItemAsyncTask(itemDAO).execute(item);
    }
    public void update(Location item) {
        new LocationRepository.UpdateItemAsyncTask(itemDAO).execute(item);
    }
    public void delete (Location item) {
        new LocationRepository.DeleteItemAsyncTask(itemDAO).execute(item);

    }
    public void deleteAllItems() {
        new LocationRepository.DeleteAllItemAsyncTask(itemDAO).execute();

    }
    public LiveData<List<Location>> getAllItems() {
        return allItems;
    }


    private static class InsertItemAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDAO itemDAO;
        private InsertItemAsyncTask(LocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Location... items) {
            itemDAO.insert(items[0]);
            return null;
        }
    }




    private static class UpdateItemAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDAO itemDAO;
        private UpdateItemAsyncTask(LocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Location... items) {
            itemDAO.update(items[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDAO itemDAO;
        private DeleteItemAsyncTask(LocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Location... items) {
            itemDAO.delete(items[0]);
            return null;
        }
    }

    private static class DeleteAllItemAsyncTask extends AsyncTask<Location, Void, Void> {
        private LocationDAO itemDAO;
        private DeleteAllItemAsyncTask(LocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Location... items) {
            itemDAO.deleteAllItems();
            return null;
        }
    }
}
