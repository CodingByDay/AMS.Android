package com.example.uhf.settings;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.uhf.database.Database;
import com.example.uhf.item.ItemRepository;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.mvvm.Model.ItemDAO;

import java.util.List;

public class SettingsRepository {
    private SettingDAO itemDAO;
    private LiveData<List<Setting>> allItems;
    public SettingsRepository(Application application) {
        Database database = Database.getInstance(application);
        itemDAO = database.settingDAO();
        allItems = itemDAO.getAllItems();
    }

    public void insert(Setting item) {
        new SettingsRepository.InsertItemAsyncTask(itemDAO).execute(item);
    }
    public void update(Setting item) {
        new SettingsRepository.UpdateItemAsyncTask(itemDAO).execute(item);
    }
    public void delete (Setting item) {
        new SettingsRepository.DeleteItemAsyncTask(itemDAO).execute(item);
    }
    public void deleteAllItems() {
        new SettingsRepository.DeleteAllItemAsyncTask(itemDAO).execute();
    }
    public LiveData<List<Setting>> getAllItems() {
        return allItems;
    }


    private static class InsertItemAsyncTask extends AsyncTask<Setting, Void, Void> {
        private SettingDAO itemDAO;
        private InsertItemAsyncTask(SettingDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Setting... items) {
            itemDAO.insert(items[0]);
            return null;
        }
    }




    private static class UpdateItemAsyncTask extends AsyncTask<Setting, Void, Void> {

        private SettingDAO itemDAO;

        private UpdateItemAsyncTask(SettingDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Setting... items) {
            itemDAO.update(items[0]);
            return null;
        }
    }




    private static class DeleteItemAsyncTask extends AsyncTask<Setting, Void, Void> {

        private SettingDAO itemDAO;

        private DeleteItemAsyncTask(SettingDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Setting... items) {
            itemDAO.delete(items[0]);
            return null;
        }
    }




    private static class DeleteAllItemAsyncTask extends AsyncTask<Setting, Void, Void> {

        private SettingDAO itemDAO;

        private DeleteAllItemAsyncTask(SettingDAO itemDAO) {
            this.itemDAO = itemDAO;
        }
        @Override
        protected Void doInBackground(Setting... items) {
            itemDAO.deleteAllItems();
            return null;
        }
    }
}
