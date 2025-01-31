package com.example.uhf.repository;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.uhf.api.AsyncCallBack;
import com.example.uhf.database.Database;
import com.example.uhf.mvvm.Model.ItemLocationCache;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.Model.LocationDAO;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository {
    private LocationDAO itemDAO;
    private LiveData<List<Location>> allItems;
    private AsyncCallBack callBack;


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


    public void insertLocationsBatch(Context context, Location... items) {
        callBack = (AsyncCallBack) context;
        new InsertLocationsBatchAsync(itemDAO).execute(items);
    }

    private class InsertLocationsBatchAsync extends AsyncTask<com.example.uhf.mvvm.Model.Location, Integer, Void> {
        private LocationDAO itemDAO;
        private InsertLocationsBatchAsync(LocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }

        @Override
        protected Void doInBackground(Location... items) {
            int breakPoint = Math.round(items.length / 100);
            int progress = 0;
            int counter = 0;
            for (Location location: items) {
                counter +=1;
                try {
                    itemDAO.insert(location);
                } catch (SQLiteConstraintException ignored) {


                    itemDAO.update(location.getName(), location.getCode(), location.getDept(), location.getLocation());
                }

                if(counter == breakPoint) {
                    progress += 1;
                    publishProgress(progress);
                    counter = 0;
                }
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            LocationRepository.this.callBack.setProgressValue(values[0]);
        }

        @Override
        protected void onPostExecute(Void unused) {
            LocationRepository.this.callBack.setProgressValue(100);
            super.onPostExecute(unused);
        }
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
            itemDAO.update(items[0].getName(), items[0].getCode(), items[0].getDept(), items[0].getLocation());
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
