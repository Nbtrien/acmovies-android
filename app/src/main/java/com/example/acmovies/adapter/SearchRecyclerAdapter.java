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

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearcViewHolder> {
    Context context;
    List<Movie> movieList;
    ItemClickListener itemClickListener;

    public SearchRecyclerAdapter(Context context, List<Movie> movieList, ItemClickListener itemClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SearcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearcViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearcViewHolder holder, int position) {
        Glide.with(context).load(movieList.get(position).getCoverimage()).into(holder.imgMovie);
        String movieName = movieList.get(position).getName().substring(0, 1).toUpperCase() + movieList.get(position).getName().substring(1);
        holder.titleMovie.setText(movieName);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class SearcViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgMovie;
        private TextView titleMovie;
        public SearcViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovie = (ImageView) itemView.findViewById(R.id.img_Movie);
            titleMovie = (TextView) itemView.findViewById(R.id.txt_Moviename);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onMovieClick(movieList.get(getAdapterPosition()));
                }
            });
        }
    }
}
