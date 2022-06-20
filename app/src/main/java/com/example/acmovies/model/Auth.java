package com.example.acmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Auth {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private User user;

    public Boolean getStatus() {
    return status;
    }

    public void setStatus(Boolean status) {
    this.status = status;
    }

    public String getToken() {
    return token;
    }

    public void setToken(String token) {
    this.token = token;
    }

    public User getUser() {
    return user;
    }

    public void setUser(User user) {
    this.user = user;
    }

}