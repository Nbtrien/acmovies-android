package com.example.acmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthWrapper<data> {
    @SerializedName("data")
    @Expose
    private data data;

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }
}
