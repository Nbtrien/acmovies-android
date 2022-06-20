package com.example.acmovies.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.ListMovie;
import com.example.acmovies.model.Movie;

import java.util.List;

public class RelatedMoviesAdapter extends RecyclerView.Adapter<RelatedMoviesAdapter.RelatedViewholder> implements ItemClickListener {

    Context context;
    List<ListMovie> listMovie;

    public RelatedMoviesAdapter(Context context, List<ListMovie> listMovie) {
        this.context = context;
        this.listMovie = listMovie;
    }

    @NonNull
    @Override
    public RelatedViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    @Override
    public void onMovieClick(Movie movie) {

    }

    @Override
    public void onEpisodeClick(Episode episode) {

    }

    @Override
    public void onActorClick(Actor actor) {

    }

    @Override
    public void onGenreClick(Genres genres) {

    }

    public class RelatedViewholder extends RecyclerView.ViewHolder{
        public RelatedViewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
