package com.example.uhf.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ProgressBar;

import com.example.uhf.activity.LoginActivityMain;
import com.example.uhf.settings.Setting;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Communicator {

    public class RetrieveLogingInformation extends AsyncTask<String, Void, Boolean> {
        private LoginActivityMain login;
        private AsyncCallBack asyncCallBack;
        RetrieveLogingInformation setInstance(Context context) {
            this.login = (LoginActivityMain) context;
            asyncCallBack = (AsyncCallBack) context;
            return this;
        }
        @Override protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override protected Boolean doInBackground(String... args) {
            int responseCode;
            try {
                URL url = new URL(args[0]);
                String json = args[1];
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.connect();
                responseCode = conn.getResponseCode();
            } catch (Exception e) {
                return false;
            }
            return responseCode == 200;
        }
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            asyncCallBack.setResult(result);
        }
    }

         String baseUrl = "";
        public boolean login(Context context, List<Setting> settings, String company, String uname, String password) throws JsonProcessingException {
            try {
                baseUrl = settings.get(0).getValue();
                String endpoint = "/login";
                User user = new User(company,uname,password);
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(user);
                String url = baseUrl + endpoint;
                RetrieveLogingInformation retrieve = new RetrieveLogingInformation();
                retrieve = retrieve.setInstance(context);
                retrieve.execute(url, json);
                return false;

            } catch (Exception e) {
                return false;
            }
        }


        public boolean isDeviceConnected(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        }

}
