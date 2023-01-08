package com.example.acmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WrapperData<data> {
    @SerializedName("data")
    @Expose
    private List<data> data;

    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<data> getData() {
        return data;
    }

    public void setData(List<data> data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }
}
