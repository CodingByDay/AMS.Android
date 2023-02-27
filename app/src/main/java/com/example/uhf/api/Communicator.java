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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
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
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.connect();
                try(OutputStream os = conn.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;

                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    // Converting to response object
                    ObjectMapper mapper = new ObjectMapper();
                    String json_c = response.toString();
                    HashMap<String, String> myMap = mapper.readValue(json_c, new TypeReference<HashMap<String, String>>() {
                    });
                    String success = myMap.get("success");
                    String token = myMap.get("token");
                    String error = myMap.get("error");
                    if(token!=null) {
                        login.token = token;
                    }
                    assert success != null;
                    if (!success.equals("true")) return false;
                    assert token != null;
                    return !token.isEmpty();
                }
            } catch (Exception e) {
                return false;
            }

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
