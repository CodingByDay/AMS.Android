package com.example.uhf.api;

public class User {
    private String company;
    private String username;
    private String password;

    public User(String company, String username, String password) {
        this.company = company;
        this.username = username;
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
