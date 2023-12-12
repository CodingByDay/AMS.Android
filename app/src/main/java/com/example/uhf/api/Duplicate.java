package com.example.uhf.api;

public class Duplicate {
    private boolean success;
    private String location;
    private String error;
    public Duplicate(boolean success, String location, String error) {
        this.success = success;
        this.location = location;
        this.error = error;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
