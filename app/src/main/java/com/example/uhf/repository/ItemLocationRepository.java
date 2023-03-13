package com.example.uhf.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.uhf.database.Database;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationDAO;

import java.util.List;

public class ItemLocationRepository {
    private final LiveData<List<ItemLocation>> allItemsRegistered;
    private ItemLocationDAO itemDAO;
    private LiveData<List<ItemLocation>> allItems;
    private LiveData<List<ItemLocation>> allItemsNotRegistered;


    public ItemLocationRepository(Application application) {
        Database database = Database.getInstance(application);
        itemDAO = database.itemlocationDAO();
        allItems = itemDAO.getAllItems();
        allItemsNotRegistered = itemDAO.getAllItemsThatAreNotRegistered();
        allItemsRegistered = itemDAO.getAllItemsThatAreRegistered();
    }

    public void insert(ItemLocation item) {
        new ItemLocationRepository.InsertItemAsyncTask(itemDAO).execute(item);
    }
    public void update(ItemLocation item) {
        new ItemLocationRepository.UpdateItemAsyncTask(itemDAO).execute(item);
    }

    public void updateById(int id, String ecd) {
        new ItemLocationRepository.UpdateItemByIdAsyncTask(itemDAO).execute(String.valueOf(id), ecd);
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


    public LiveData<List<ItemLocation>> getAllItemsRegistered() {return allItemsRegistered;}





    private static class GetItemLocationByEcdAsyncTask extends AsyncTask<String, Void, ItemLocation> {

        private ItemLocationDAO itemDAO;

        private GetItemLocationByEcdAsyncTask(ItemLocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        protected ItemLocation doInBackground(String id) {

            return  itemDAO.getItemByEcd(id);
        }

        @Override
        protected ItemLocation doInBackground(String... strings) {
            return  itemDAO.getItemByEcd(strings[0]);

        }
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


    private static class UpdateItemByIdAsyncTask extends AsyncTask<String, Void, Void> {
        private ItemLocationDAO itemDAO;
        private UpdateItemByIdAsyncTask(ItemLocationDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(String... items) {
            int id = Integer.parseInt(items[0]);
            String epc = items[1];
            itemDAO.updateByID(id, epc);
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
