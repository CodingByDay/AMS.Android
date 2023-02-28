package com.example.uhf.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.uhf.database.Database;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationDAO;

import java.util.List;

public class ItemLocationRepository {
    private ItemLocationDAO itemDAO;
    private LiveData<List<ItemLocation>> allItems;
    private LiveData<List<ItemLocation>> allItemsNotRegistered;


    public ItemLocationRepository(Application application) {
        Database database = Database.getInstance(application);
        itemDAO = database.itemlocationDAO();
        allItems = itemDAO.getAllItems();
        allItemsNotRegistered = itemDAO.getAllItemsThatAreNotRegistered();
    }

    public void insert(ItemLocation item) {
        new ItemLocationRepository.InsertItemAsyncTask(itemDAO).execute(item);
    }
    public void update(ItemLocation item) {
        new ItemLocationRepository.UpdateItemAsyncTask(itemDAO).execute(item);
    }
    public void delete (ItemLocation item) {
        new ItemLocationRepository.DeleteItemAsyncTask(itemDAO).execute(item);
    }
    public void deleteAllItems() {
        new ItemLocationRepository.DeleteAllItemAsyncTask(itemDAO).execute();

    }
    public LiveData<List<ItemLocation>> getAllItems() {
        return allItems;
    }


    public LiveData<List<ItemLocation>> getAllItemsNotRegistered() {
        return allItemsNotRegistered;
    }




    private static class InsertItemAsyncTask extends AsyncTask<ItemLocation, Void, Void> {

        private ItemLocationDAO itemDAO;

        private InsertItemAsyncTask(ItemLocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(ItemLocation... items) {
            itemDAO.insert(items[0]);
            return null;
        }
    }




    private static class UpdateItemAsyncTask extends AsyncTask<ItemLocation, Void, Void> {

        private ItemLocationDAO itemDAO;

        private UpdateItemAsyncTask(ItemLocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(ItemLocation... items) {
            itemDAO.update(items[0]);
            return null;
        }
    }




    private static class DeleteItemAsyncTask extends AsyncTask<ItemLocation, Void, Void> {
        private ItemLocationDAO itemDAO;
        private DeleteItemAsyncTask(ItemLocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(ItemLocation... items) {
            itemDAO.delete(items[0]);
            return null;
        }
    }




    private static class DeleteAllItemAsyncTask extends AsyncTask<ItemLocation, Void, Void> {

        private ItemLocationDAO itemDAO;
        private DeleteAllItemAsyncTask(ItemLocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(ItemLocation... items) {
            itemDAO.deleteAllItems();
            return null;
        }
    }
}
