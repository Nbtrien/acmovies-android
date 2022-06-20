package com.example.acmovies.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.R;
import com.example.acmovies.model.MovieProperties;

import java.util.List;

public class MoviePropertiesAdapter extends RecyclerView.Adapter<MoviePropertiesAdapter.MoviePropertiesViewHolder>{
    Context context;
    List<MovieProperties> movieProperties;

    public MoviePropertiesAdapter(Context context, List<MovieProperties> movieProperties) {
        this.context = context;
        this.movieProperties = movieProperties;
    }

    @NonNull
    @Override
    public MoviePropertiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MoviePropertiesViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_infor_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePropertiesViewHolder holder, int position)
    {
        holder.txt_properties.setText(movieProperties.get(position).getProperties());
        holder.txt_value.setText(movieProperties.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return movieProperties.size();
    }

    public class MoviePropertiesViewHolder extends RecyclerView.ViewHolder{
        TextView txt_properties, txt_value;

        public MoviePropertiesViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_properties = (TextView) itemView.findViewById(R.id.txt_properties);
            txt_value = (TextView) itemView.findViewById(R.id.txt_value);
        }
    }
}

