package com.example.uhf.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.uhf.api.AsyncCallBack;
import com.example.uhf.database.Database;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemDAO;
import com.example.uhf.mvvm.Model.Location;
import com.example.uhf.mvvm.Model.LocationDAO;

import java.util.List;

public class ItemRepository {
    private ItemDAO itemDAO;
    private LiveData<List<Item>> allItems;
    private AsyncCallBack callBack;


    public ItemRepository(Application application) {
        Database database = Database.getInstance(application);
        itemDAO = database.itemDAO();
        allItems = itemDAO.getAllItems();
    }

    public void batchInsert(Item... items) {

    }
    public void insert(Item item) {
        new InsertItemAsyncTask(itemDAO).execute(item);
    }
    public void update(Item item) {
        new UpdateItemAsyncTask(itemDAO).execute(item);
    }
    public void delete (Item item) {
        new DeleteItemAsyncTask(itemDAO).execute(item);

    }
    public void deleteAllItems() {
        new DeleteAllItemAsyncTask(itemDAO).execute();

    }
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }





    public void insertItemsBatch(Context context, Item... items) {
        callBack = (AsyncCallBack) context;
        new ItemRepository.InsertItemsBatchAsync(itemDAO).execute(items);
    }

    public class InsertItemsBatchAsync extends AsyncTask<com.example.uhf.mvvm.Model.Item, Void, Void> {
        private ItemDAO itemDAO;
        private InsertItemsBatchAsync(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }


        @Override
        protected Void doInBackground(Item... items) {
            for (Item item: items) {
                itemDAO.insert(item);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            ItemRepository.this.callBack.setProgressValue(75);
            super.onPostExecute(unused);
        }
    }






    private static class InsertItemAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDAO itemDAO;

        private InsertItemAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.insert(items[0]);
            return null;
        }
    }





    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDAO itemDAO;

        private UpdateItemAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.update(items[0]);
            return null;
        }
    }




    private static class DeleteItemAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDAO itemDAO;

        private DeleteItemAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.delete(items[0]);
            return null;
        }
    }




    private static class DeleteAllItemAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDAO itemDAO;

        private DeleteAllItemAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.deleteAllItems();
            return null;
        }
    }
}
