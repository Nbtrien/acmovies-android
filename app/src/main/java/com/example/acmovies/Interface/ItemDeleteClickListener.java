package com.example.acmovies.Interface;

import com.example.acmovies.model.Movie;
import com.example.acmovies.model.UserView;

public interface ItemDeleteClickListener {
    void deleteMovieClick(UserView userView);
    void deleteMovieClick(Movie movie);
}
