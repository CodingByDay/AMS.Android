package com.example.uhf.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.uhf.database.Database;
import com.example.uhf.mvvm.Model.ItemTemporary;
import com.example.uhf.mvvm.Model.ItemTemporaryDAO;

import java.util.List;

public class ItemTemporaryRepository {

    private ItemTemporaryDAO itemTemporaryDAO;
    private LiveData<List<ItemTemporary>> allItems;


    public ItemTemporaryRepository(Application application) {
        Database database = Database.getInstance(application);
        itemTemporaryDAO = database.itemTemporaryDAO();
        allItems = itemTemporaryDAO.getAllItems();
    }

    public void insert(ItemTemporary item) {
        new ItemTemporaryRepository.InsertItemAsyncTask(itemTemporaryDAO).execute(item);
    }
    public void update(ItemTemporary item) {
        new ItemTemporaryRepository.UpdateItemAsyncTask(itemTemporaryDAO).execute(item);
    }
    public void delete (ItemTemporary item) {
        new ItemTemporaryRepository.DeleteItemAsyncTask(itemTemporaryDAO).execute(item);

    }
    public void deleteAllItems() {
        new ItemTemporaryRepository.DeleteAllItemAsyncTask(itemTemporaryDAO).execute();

    }
    public LiveData<List<ItemTemporary>> getAllItems() {
        return allItems;
    }


    private static class InsertItemAsyncTask extends AsyncTask<ItemTemporary, Void, Void> {

        private ItemTemporaryDAO itemTemporaryDAO;

        private InsertItemAsyncTask(ItemTemporaryDAO itemTemporaryDAO) {
            this.itemTemporaryDAO = itemTemporaryDAO;
        }
        @Override
        protected Void doInBackground(ItemTemporary... items) {
            itemTemporaryDAO.insert(items[0]);
            return null;
        }
    }




    private static class UpdateItemAsyncTask extends AsyncTask<ItemTemporary, Void, Void> {

        private ItemTemporaryDAO itemTemporaryDAO;

        private UpdateItemAsyncTask(ItemTemporaryDAO itemTemporaryDAO) {
            this.itemTemporaryDAO = itemTemporaryDAO;
        }
        @Override
        protected Void doInBackground(ItemTemporary... items) {
            itemTemporaryDAO.update(items[0]);
            return null;
        }
    }




    private static class DeleteItemAsyncTask extends AsyncTask<ItemTemporary, Void, Void> {

        private ItemTemporaryDAO itemTemporaryDAO;

        private DeleteItemAsyncTask(ItemTemporaryDAO itemTemporaryDAO) {
            this.itemTemporaryDAO = itemTemporaryDAO;
        }
        @Override
        protected Void doInBackground(ItemTemporary... items) {
            itemTemporaryDAO.delete(items[0]);
            return null;
        }
    }




    private static class DeleteAllItemAsyncTask extends AsyncTask<ItemTemporary, Void, Void> {

        private ItemTemporaryDAO itemTemporaryDAO;

        private DeleteAllItemAsyncTask(ItemTemporaryDAO itemTemporaryDAO) {
            this.itemTemporaryDAO = itemTemporaryDAO;
        }
        @Override
        protected Void doInBackground(ItemTemporary... items) {
            itemTemporaryDAO.deleteAllItems();
            return null;
        }
    }
}
