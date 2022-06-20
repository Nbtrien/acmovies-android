package com.example.acmovies.model;

import java.io.Serializable;
import java.util.List;

import com.example.acmovies.model.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListMovie implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("movies")
    @Expose
    private List<Movie> movies = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

}