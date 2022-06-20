package com.example.acmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.R;
import com.example.acmovies.model.Movie;

import java.util.List;

public class MovieListRecyclerAdapter extends RecyclerView.Adapter<MovieListRecyclerAdapter.MovieListViewHolder>{
    Context context;
    List<Movie> movieList;
    ItemClickListener itemClickListener;

    public MovieListRecyclerAdapter(Context context, List<Movie> movieList, ItemClickListener itemClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MovieListRecyclerAdapter.MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieListRecyclerAdapter.MovieListViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_recycler_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListRecyclerAdapter.MovieListViewHolder holder, int position)
    {
        holder.txt_film.setText(movieList.get(position).getName());
        if (movieList.get(position).getProfileimage() != null){
            Glide.with(context).load(movieList.get(position).getProfileimage().getImageUrl()).into(holder.img_film);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieListViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_film;
        TextView txt_film;
        public MovieListViewHolder(@NonNull View itemView) {
            super(itemView);
            img_film = (ImageView) itemView.findViewById(R.id.img_item_film);
            txt_film = (TextView) itemView.findViewById(R.id.txt_item_film);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onMovieClick(movieList.get(getAdapterPosition()));
                }
            });

        }
    }
}
