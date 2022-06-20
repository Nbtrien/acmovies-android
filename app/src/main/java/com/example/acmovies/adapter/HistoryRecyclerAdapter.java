package com.example.acmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder> {
    Context context;
    List<UserView> userViewList;
    ItemClickListener itemClickListener;
    ItemDeleteClickListener itemDeleteClickListener;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public HistoryRecyclerAdapter(Context context, List<UserView> userViewList, ItemClickListener itemClickListener, ItemDeleteClickListener itemDeleteClickListener) {
        this.context = context;
        this.userViewList = userViewList;
        this.itemClickListener = itemClickListener;
        this.itemDeleteClickListener = itemDeleteClickListener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.history_movie_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Glide.with(context).load(userViewList.get(position).getMovie().getCoverimage().getImageUrl()).into(holder.imgMovie);
        holder.txtName.setText(userViewList.get(position).getMovie().getName());
        if (userViewList.get(position).getEpisode() != null) {
            String episodeTitle = "";
            if (userViewList.get(position).getEpisode().getTitle() != null){
                episodeTitle = userViewList.get(position).getEpisode().getTitle();
            }
            holder.txtEpisode.setText("Tập " + userViewList.get(position).getEpisode().getEpisode() + ": " + episodeTitle);
        }
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(userViewList.get(position)));
        viewBinderHelper.closeLayout(String.valueOf(userViewList.get(position)));
    }

    @Override
    public int getItemCount() {
        return userViewList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtEpisode, txtDelete;
        private ImageView imgMovie;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout movieLayout;

        public HistoryViewHolder(@NonNull View itemView) {
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
                    itemClickListener.onMovieClick(userViewList.get(getAdapterPosition()).getMovie());
                }
            });
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemDeleteClickListener.deleteMovieClick(userViewList.get(getAdapterPosition()));
                }
            });
        }
    }
}
