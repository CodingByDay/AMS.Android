package com.example.uhf.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.uhf.api.AsyncCallBack;
import com.example.uhf.database.Database;
import com.example.uhf.mvvm.Model.Asset;
import com.example.uhf.mvvm.Model.AssetDAO;
import com.example.uhf.mvvm.Model.ItemLocation;
import com.example.uhf.mvvm.Model.ItemLocationDAO;

import java.util.List;

public class AssetRepository {

    private AssetDAO itemDAO;
    private LiveData<List<Asset>> allItems;
    private AsyncCallBack callBack;
    private int count;


    public AssetRepository(Application application) {
        Database database = Database.getInstance(application);
        itemDAO = database.assetDAO();
        allItems = itemDAO.getAllItems();
    }

    public void insert(Asset item) {
        new AssetRepository.InsertItemAsyncTask(itemDAO).execute(item);
    }
    public void update(Asset item) {
        new AssetRepository.UpdateItemAsyncTask(itemDAO).execute(item);
    }


    public void delete (Asset item) {
        new AssetRepository.DeleteItemAsyncTask(itemDAO).execute(item);
    }
    public void deleteAllItems() {
        new AssetRepository.DeleteAllItemAsyncTask(itemDAO).execute();

    }
    public LiveData<List<Asset>> getAllItems() {
        return allItems;
    }

    private static class InsertItemAsyncTask extends AsyncTask<Asset, Void, Void> {

        private AssetDAO itemDAO;

        private InsertItemAsyncTask(AssetDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Asset... items) {
            itemDAO.insert(items[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<Asset, Void, Void> {
        private AssetDAO itemDAO;
        private UpdateItemAsyncTask(AssetDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Asset... items) {
            itemDAO.update(items[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<Asset, Void, Void> {
        private AssetDAO itemDAO;
        private DeleteItemAsyncTask(AssetDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Asset... items) {
            itemDAO.delete(items[0]);
            return null;
        }
    }

    private static class DeleteAllItemAsyncTask extends AsyncTask<Asset, Void, Void> {

        private AssetDAO itemDAO;
        private DeleteAllItemAsyncTask(AssetDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Asset... items) {
            itemDAO.deleteAllItems();
            return null;
        }
    }

}
