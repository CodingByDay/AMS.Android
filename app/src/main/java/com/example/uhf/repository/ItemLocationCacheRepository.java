package com.example.uhf.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.uhf.database.Database;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationCache;
import com.example.uhf.mvvm.Model.ItemLocationCacheDAO;
import com.example.uhf.mvvm.Model.ItemLocationDAO;

import java.util.List;

public class ItemLocationCacheRepository {
    private ItemLocationCacheDAO itemDAO;
    private LiveData<List<ItemLocationCache>> allItems;
    private LiveData<List<ItemLocationCache>> allItemsNotRegistered;


    public ItemLocationCacheRepository(Application application) {
        Database database = Database.getInstance(application);
        itemDAO = database.itemLocationCacheDAO();
        allItems = itemDAO.getAllItems();

    }

    public void insert(ItemLocationCache item) {
        new ItemLocationCacheRepository.InsertItemAsyncTask(itemDAO).execute(item);
    }
    public void update(ItemLocationCache item) {
        new ItemLocationCacheRepository.UpdateItemAsyncTask(itemDAO).execute(item);
    }
    public void delete (ItemLocationCache item) {
        new ItemLocationCacheRepository.DeleteItemAsyncTask(itemDAO).execute(item);
    }
    public void deleteAllItems() {
        new ItemLocationCacheRepository.DeleteAllItemAsyncTask(itemDAO).execute();

    }
    public LiveData<List<ItemLocationCache>> getAllItems() {
        return allItems;
    }


    public LiveData<List<ItemLocationCache>> getAllItemsNotRegistered() {
        return allItemsNotRegistered;
    }




    private static class InsertItemAsyncTask extends AsyncTask<ItemLocationCache, Void, Void> {

        private ItemLocationCacheDAO itemDAO;

        private InsertItemAsyncTask(ItemLocationCacheDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(ItemLocationCache... items) {
            itemDAO.insert(items[0]);
            return null;
        }
    }




    private static class UpdateItemAsyncTask extends AsyncTask<ItemLocationCache, Void, Void> {

        private ItemLocationCacheDAO itemDAO;

        private UpdateItemAsyncTask(ItemLocationCacheDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(ItemLocationCache... items) {
            itemDAO.update(items[0]);
            return null;
        }
    }




    private static class DeleteItemAsyncTask extends AsyncTask<ItemLocationCache, Void, Void> {
        private ItemLocationCacheDAO itemDAO;
        private DeleteItemAsyncTask(ItemLocationCacheDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(ItemLocationCache... items) {
            itemDAO.delete(items[0]);
            return null;
        }
    }




    private static class DeleteAllItemAsyncTask extends AsyncTask<ItemLocationCache, Void, Void> {

        private ItemLocationCacheDAO itemDAO;
        private DeleteAllItemAsyncTask(ItemLocationCacheDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(ItemLocationCache... items) {
            itemDAO.deleteAllItems();
            return null;
        }
    }
}
