package com.example.acmovies.Interface;

import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.Movie;

public interface ItemClickListener {

    void onMovieClick(Movie movie);
    void onEpisodeClick(Episode episode);
    void onActorClick(Actor actor);
    void onGenreClick(Genres genres);
}
