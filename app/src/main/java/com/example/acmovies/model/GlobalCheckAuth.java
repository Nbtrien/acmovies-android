package com.example.acmovies.model;

import android.app.Application;

public class GlobalCheckAuth extends Application {
    private boolean isLogged = false;
    private String token;
    private User user;

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void onCreate() {
        //reinitialize variable
        super.onCreate();
    }
}
