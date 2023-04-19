package com.example.uhf.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.uhf.api.AsyncCallBack;
import com.example.uhf.database.Database;
import com.example.uhf.mvvm.Model.Asset;
import com.example.uhf.mvvm.Model.AssetDAO;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.CheckOutDAO;

import java.util.List;

public class CheckOutRepository {
    private CheckOutDAO itemDAO;
    private LiveData<List<CheckOut>> allItems;
    private AsyncCallBack callBack;
    private int count;
    public CheckOutRepository(Application application) {
        Database database = Database.getInstance(application);
        itemDAO = database.checkOutDAO();
        allItems = itemDAO.getAllItems();
    }

    public void insert(CheckOut item) {
        new CheckOutRepository.InsertItemAsyncTask(itemDAO).execute(item);
    }
    public void update(CheckOut item) {
        new CheckOutRepository.UpdateItemAsyncTask(itemDAO).execute(item);
    }
    public void delete (CheckOut item) {
        new CheckOutRepository.DeleteItemAsyncTask(itemDAO).execute(item);
    }

    public void deleteAllItems() {
        new CheckOutRepository.DeleteAllItemAsyncTask(itemDAO).execute();
    }

    public LiveData<List<CheckOut>> getAllItems() {
        return allItems;
    }

    private static class InsertItemAsyncTask extends AsyncTask<CheckOut, Void, Void> {
        private CheckOutDAO itemDAO;
        private InsertItemAsyncTask(CheckOutDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(CheckOut... items) {
            itemDAO.insert(items[0]);
            return null;
        }
    }
    private static class UpdateItemAsyncTask extends AsyncTask<CheckOut, Void, Void> {
        private CheckOutDAO itemDAO;
        private UpdateItemAsyncTask(CheckOutDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(CheckOut... items) {
            itemDAO.update(items[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<CheckOut, Void, Void> {
        private CheckOutDAO itemDAO;
        private DeleteItemAsyncTask(CheckOutDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(CheckOut... items) {
            itemDAO.delete(items[0]);
            return null;
        }
    }
    private static class DeleteAllItemAsyncTask extends AsyncTask<CheckOut, Void, Void> {
        private CheckOutDAO itemDAO;
        private DeleteAllItemAsyncTask(CheckOutDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(CheckOut... items) {
            itemDAO.deleteAllItems();
            return null;
        }
    }

}
