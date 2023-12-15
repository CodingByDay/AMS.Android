package com.example.uhf.api;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncResponse {
    private boolean success;
    private String error;
    public SyncResponse(boolean success, String error) {
        this.success = success;
        this.error = error;
    }
    public SyncResponse() {
        this.success = false;
    }
    public static SyncResponse fromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        boolean success = jsonObject.getBoolean("success");
        String error = jsonObject.getString("error");
        return new SyncResponse(success, error);
    }
    public boolean isSuccess() {
        return success;
    }
    public String getError() {
        return error;
    }
}
