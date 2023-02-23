package com.example.uhf.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.uhf.mvvm.ViewModel.SettingsViewModel;
import com.example.uhf.settings.Setting;
import com.example.uhf.settings.SettingsHelper;

import java.util.List;

public class Communicator {


    public static class CommunicatorAPI {

        SettingsViewModel settingsModel;
        public static boolean login(List<Setting> settings, String company, String uname, String password) {

           boolean jesus = true;




            return jesus;
        }


        public static boolean isDeviceConnected(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        }
    }
}
