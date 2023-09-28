package com.example.uhf.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.uhf.activity.LoginActivityMain;
import com.example.uhf.mvvm.Model.CheckOut;
import com.example.uhf.mvvm.Model.Item;
import com.example.uhf.settings.Setting;
import com.example.uhf.tools.DateHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;
import com.microsoft.appcenter.distribute.DistributeListener;
import com.microsoft.appcenter.distribute.ReleaseDetails;
import com.microsoft.appcenter.distribute.UpdateAction;
public class Communicator {

    public class CommitCheckOut extends AsyncTask<String, Void, Boolean> {

        private AsyncCallBack asyncCallBack;
        CommitCheckOut setInstance(Context context) {

            asyncCallBack = (AsyncCallBack) context;
            return this;
        }
        @Override protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override protected Boolean doInBackground(String... args) {
            int responseCode;
            try {
                URL url = new URL("http://riko-inv.in-sist.si");
                String json = new String("");
                for(String ar: args) {
                    if(ar.startsWith("http")) {
                        url = new URL(ar);
                    } else {
                        json = ar;
                    }
                }

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
                    Analytics.trackEvent(json_c);
                    if(json_c.contains("\"success\":true")) {
                        return true;
                    }
                    return false;

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
                URL url = new URL("http://riko-inv.in-sist.si");
                String json = new String("");
                for(String ar: args) {
                    if(ar.startsWith("http")) {
                        url = new URL(ar);
                    } else {
                        json = ar;
                    }
                }
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
                    String token = myMap.get("result");
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
    /**
     * This class is responsible for handling item synchronization
     */
    public class RetrieveItems extends AsyncTask<String, Void, Root> {
        private LoginActivityMain login;
        private AsyncCallBack asyncCallBack;
        RetrieveItems setInstance(Context context) {
            asyncCallBack = (AsyncCallBack) context;
            return this;
        }
        @Override protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override protected Root doInBackground(String... args) {
            int responseCode;
            try {
                URL url = new URL("http://riko-inv.in-sist.si");
                String json = new String("");
                for(String ar: args) {
                    if(ar.startsWith("http")) {
                        url = new URL(ar);
                    } else {
                        json = ar;
                    }
                }
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
                    ObjectMapper om = new ObjectMapper();
                    Root root = om.readValue(response.toString(), Root.class);
                    return root;
                }
            } catch (Exception e) {
                return new Root();
            }
        }
        protected void onPostExecute(Root result) {
            super.onPostExecute(result);
            asyncCallBack.setResultRoot(result);
        }
    }
    /**
     * This class is responsible for handling locations synchronization
     */
    public class RetrieveLocations extends AsyncTask<String, Void, RootLocation > {
        private LoginActivityMain login;
        private AsyncCallBack asyncCallBack;
        RetrieveLocations setInstance(Context context) {
            asyncCallBack = (AsyncCallBack) context;
            return this;
        }
        @Override protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override protected RootLocation doInBackground(String... args) {
            int responseCode;
            try {
                URL url = new URL("http://riko-inv.in-sist.si");
                String json = new String("");
                for(String ar: args) {
                    if(ar.startsWith("http")) {
                       url = new URL(ar);
                    } else {
                        json = ar;
                    }
                }

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
                    ObjectMapper om = new ObjectMapper();
                    RootLocation root = om.readValue(response.toString(), RootLocation.class);
                    return root;
                }
            } catch (Exception e) {
                return new RootLocation();
            }
        }
        protected void onPostExecute(RootLocation result) {
            super.onPostExecute(result);
            asyncCallBack.setResultRootLocation(result);
        }
    }
    /**
     * This class is responsible for handling locations synchronization
     */
    public class RetrieveStatus extends AsyncTask<String, Void, RootStatus > {
        private LoginActivityMain login;
        private AsyncCallBack asyncCallBack;
        RetrieveStatus setInstance(Context context) {
            asyncCallBack = (AsyncCallBack) context;
            return this;
        }
        @Override protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override protected RootStatus doInBackground(String... args) {
            int responseCode;
            try {
                URL url = new URL("http://riko-inv.in-sist.si");
                String json = new String("");
                for(String ar: args) {
                    if(ar.startsWith("http")) {
                        url = new URL(ar);
                    } else {
                        json = ar;
                    }
                }
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
                    ObjectMapper om = new ObjectMapper();
                    RootStatus root = om.readValue(response.toString(), RootStatus.class);
                    return root;
                }
            } catch (Exception e) {
                return new RootStatus();

            }

        }
        protected void onPostExecute(RootStatus result) {
            super.onPostExecute(result);
            asyncCallBack.setResultRootStatus(result);
        }
    }
    /**
     * This class is responsible for handling locations synchronization
     */
    public class RetrieveAsset extends AsyncTask<String, Void, RootAsset > {
        private LoginActivityMain login;
        private AsyncCallBack asyncCallBack;
        RetrieveAsset setInstance(Context context) {
            asyncCallBack = (AsyncCallBack) context;
            return this;
        }
        @Override protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override protected RootAsset doInBackground(String... args) {
            int responseCode;
            try {
                URL url = new URL("http://riko-inv.in-sist.si");
                String json = new String("");
                for(String ar: args) {
                    if(ar.startsWith("http")) {
                        url = new URL(ar);
                    } else {
                        json = ar;
                    }
                }
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

                    String js = response.toString();
                    // Converting to response object
                    ObjectMapper om = new ObjectMapper();
                    RootAsset root = om.readValue(response.toString(), RootAsset.class);
                    int result = 7 + 3;
                    return root;
                }
            } catch (Exception e) {
                return new RootAsset();

            }

        }
        protected void onPostExecute(RootAsset result) {
            super.onPostExecute(result);
            asyncCallBack.setResultRootAsset(result);
        }
    }
    // Retrieve item class
    public boolean retrieveItems(Context context, List<Setting> settings) throws JsonProcessingException {
        try {
            String token = "";
            for (Setting setting: settings) {
                if(setting.getValue().startsWith("http")) {
                    baseUrl = setting.getValue();
                } else if (setting.getValue().length() == 36 && setting.getValue().contains("-")) {
                    token = setting.getValue();
                }
            }



            String endpoint = "/getItems";

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(new Token(token));
            String url = baseUrl + endpoint;
            RetrieveItems retrieve = new RetrieveItems();
            retrieve = retrieve.setInstance(context);
            retrieve.execute(url, json);
            return false;

        } catch (Exception e) {
            return false;
        }
    }
    // Retrieve item class
    public boolean retrieveLocations(Context context, List<Setting> settings) throws JsonProcessingException {
        try {
            String baseUrl = new String();
            String token = new String();
            for (Setting setting: settings) {
                if(setting.getValue().startsWith("http")) {
                    baseUrl = setting.getValue();
                } else if (setting.getValue().length() == 36 && setting.getValue().contains("-")) {
                    token = setting.getValue();
                }
            }


            String endpoint = "/getLocations";

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(new Token(token));
            String url = baseUrl + endpoint;
            RetrieveLocations retrieve = new RetrieveLocations();
            retrieve = retrieve.setInstance(context);
            retrieve.execute(url, json);
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    // Retrieve status class
    public boolean retrieveStatus(Context context, List<Setting> settings) throws JsonProcessingException {
        try {
            String baseUrl = new String();
            String token = new String();
            for (Setting setting: settings) {
                if(setting.getValue().startsWith("http")) {
                    baseUrl = setting.getValue();
                } else if (setting.getValue().length() == 36 && setting.getValue().contains("-")) {
                    token = setting.getValue();
                }
            }
            String endpoint = "/getStatuses";

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(new Token(token));
            String url = baseUrl + endpoint;
            RetrieveStatus retrieve = new RetrieveStatus();
            retrieve = retrieve.setInstance(context);
            retrieve.execute(url, json);
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    // Retrieve assets class
    public boolean retrieveAssets(Context context, List<Setting> settings) throws JsonProcessingException {
        try {
            String baseUrl = "";
            String token = "";

            for (Setting setting: settings) {
                if(setting.getValue().startsWith("http")) {
                    baseUrl = setting.getValue();
                } else if (setting.getValue().length() == 36 && setting.getValue().contains("-")) {
                    token = setting.getValue();
                }
            }
            String endpoint = "/getAssets";
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(new Token(token));
            String url = baseUrl + endpoint;
            RetrieveAsset retrieve = new RetrieveAsset();
            retrieve = retrieve.setInstance(context);
            retrieve.execute(url, json);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

         String baseUrl = "";
        // Retrieve login information
        public boolean login(Context context, List<Setting> settings, String company, String uname, String password) throws JsonProcessingException {
            try {
                String baseUrl = new String();
                String token = new String();
                for (Setting setting: settings) {
                    if(setting.getValue().startsWith("http")) {
                        baseUrl = setting.getValue();
                    } else if (setting.getValue().length() == 36 && setting.getValue().contains("-")) {
                        token = setting.getValue();
                    }
                }
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

    public boolean checkOutCommit(Context context, List<Setting> settings, CheckOut out) throws JsonProcessingException {
        try {
            String baseUrl = new String();
            String token = new String();
            for (Setting setting: settings) {
                if(setting.getValue().startsWith("http")) {
                    baseUrl = setting.getValue();
                } else if (setting.getValue().length() == 36 && setting.getValue().contains("-")) {
                    token = setting.getValue();
                }
            }


            String currentCorrectFormat = DateHelper.DateHelperClassStaticHelper.getCurrentDateWithT();
            String endpoint = "/insertCheckOut";
            com.example.uhf.api.CheckOut ck = new com.example.uhf.api.CheckOut();
            ck.token = token;
            ck.assetID = out.getAnAssetID();
            ck.dateCheck = currentCorrectFormat;
            ck.dateConfirm = null;
            ck.timeChg = currentCorrectFormat;
            ck.timeIns = currentCorrectFormat;
            ck.code = out.getAcCode();
            ck.eCD = out.getAcECD();
            ck.item = out.getAcItem();
            ck.location = out.getAcLocation();
            ck.name = out.getAcName();
            ck.name2 = out.getAcName2();
            ck.userIns = out.getAnUserIns();
            ck.userChg = out.getAnUserChg();
            ck.note = out.getAcNote();
            ck.userCheck = out.getAnUserCheck();
            ck.userConfirm = out.getAnUserConfirm();
            ck.inventory = 1;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(ck);
            String url = baseUrl + endpoint;
            CommitCheckOut retrieve = new CommitCheckOut();
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
