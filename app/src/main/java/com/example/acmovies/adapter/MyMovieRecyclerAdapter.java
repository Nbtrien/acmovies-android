package com.example.acmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.Interface.ItemDeleteClickListener;
import com.example.acmovies.R;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.UserView;

import java.util.List;

public class MyMovieRecyclerAdapter extends RecyclerView.Adapter<MyMovieRecyclerAdapter.MyMovieViewHolder> {
    Context context;
    List<Movie> movieList;
    ItemClickListener itemClickListener;
    ItemDeleteClickListener itemDeleteClickListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public MyMovieRecyclerAdapter(Context context, List<Movie> movieList, ItemClickListener itemClickListener, ItemDeleteClickListener itemDeleteClickListener) {
        this.context = context;
        this.movieList = movieList;
        this.itemClickListener = itemClickListener;
        this.itemDeleteClickListener = itemDeleteClickListener;
    }

    @NonNull
    @Override
    public MyMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyMovieViewHolder(LayoutInflater.from(context).inflate(R.layout.history_movie_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyMovieViewHolder holder, int position) {
        Glide.with(context).load(movieList.get(position).getCoverimage().getImageUrl()).into(holder.imgMovie);
        holder.txtName.setText(movieList.get(position).getName());
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(movieList.get(position)));
        viewBinderHelper.closeLayout(String.valueOf(movieList.get(position)));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyMovieViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtEpisode, txtDelete;
        private ImageView imgMovie;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout movieLayout;
        public MyMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtMovieName);
            txtEpisode = itemView.findViewById(R.id.txtEpisode);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            imgMovie = itemView.findViewById(R.id.imgMovie);
            swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);
            movieLayout = itemView.findViewById(R.id.movieLayout);

            movieLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onMovieClick(movieList.get(getAdapterPosition()));
                }
            });
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemDeleteClickListener.deleteMovieClick(movieList.get(getAdapterPosition()));
                }
            });
        }
    }
}
