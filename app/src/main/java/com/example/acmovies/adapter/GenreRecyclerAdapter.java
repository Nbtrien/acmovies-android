package com.example.acmovies.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.R;
import com.example.acmovies.model.Genre;

import java.util.List;
import java.util.Random;

public class GenreRecyclerAdapter extends RecyclerView.Adapter<GenreRecyclerAdapter.GenreViewholder> {
    Context context;
    List<Genre> genresList;
    ItemClickListener itemClickListener;

    public GenreRecyclerAdapter(Context context, List<Genre> genresList, ItemClickListener itemClickListener) {
        this.context = context;
        this.genresList = genresList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public GenreViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreViewholder(LayoutInflater.from(context).inflate(R.layout.cardview_genre_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewholder holder, int position) {
        Random r = new Random();
        int red=r.nextInt(255 - 0 + 1)+0;
        int green=r.nextInt(255 - 0 + 1)+0;
        int blue=r.nextInt(255 - 0 + 1)+0;

        GradientDrawable draw = new GradientDrawable();
        draw.setColor(Color.rgb(red,green,blue));
        draw.setCornerRadius(10);

        holder.txt_genre.setText(genresList.get(position).getName().substring(0, 1).toUpperCase() + genresList.get(position).getName().substring(1));
        holder.card_genre.setBackground(draw);
        Glide.with(context).load(genresList.get(position).getImage().getImageUrl()).into(holder.img_genre);
    }

    @Override
    public int getItemCount() {
        return genresList.size();
    }

    public class GenreViewholder extends RecyclerView.ViewHolder{
        TextView txt_genre;
        ImageView img_genre;
        CardView card_genre;
        public GenreViewholder(@NonNull View itemView) {
            super(itemView);
            txt_genre = (TextView) itemView.findViewById(R.id.txt_genre);
            img_genre = (ImageView) itemView.findViewById(R.id.img_genre);
            card_genre = (CardView) itemView.findViewById(R.id.card_genre);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onGenreClick(genresList.get(getAdapterPosition()));
                }
            });
        }
    }
}
