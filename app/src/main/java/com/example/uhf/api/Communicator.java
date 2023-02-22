package com.example.uhf.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Communicator {


    public static class CommunicatorAPI {




        public static boolean isDeviceConnected(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        }
    }
}
