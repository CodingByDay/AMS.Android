package com.example.uhf.api;

import androidx.annotation.Nullable;

public class UserResponse {
    private boolean success;
    @Nullable
    private String error;
    private String token;


    public UserResponse(boolean success, @Nullable String error, String token) {
        this.success = success;
        this.error = error;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Nullable
    public Object getError() {
        return error;
    }

    public void setError(@Nullable String error) {
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
