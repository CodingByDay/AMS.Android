package com.example.uhf.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SyncResponse {
    private boolean success;
    private List<SyncError> errors;

    public SyncResponse(boolean success, List<SyncError> errors) {
        this.success = success;
        this.errors = errors;
    }

    public SyncResponse() {
        this.success = false;
        this.errors = new ArrayList<>();
    }

    public static SyncResponse fromJson(String jsonString) throws JSONException {
        JSONArray errorsArray = new JSONArray();
        JSONObject jsonObject = new JSONObject(jsonString);
        List<SyncError> errorsList = new ArrayList<>();

        boolean success = jsonObject.getBoolean("success");
        try {
            errorsArray = jsonObject.getJSONArray("errors");
        } catch (JSONException e) {

        }

        for (int i = 0; i < errorsArray.length(); i++) {
            JSONObject errorObj = errorsArray.getJSONObject(i);
            int index = errorObj.getInt("index");
            int qid = errorObj.getInt("qid");
            String message = errorObj.getString("message");
            errorsList.add(new SyncError(index, qid, message));
        }

        return new SyncResponse(success, errorsList);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<SyncError> getErrors() {
        return errors;
    }
}
