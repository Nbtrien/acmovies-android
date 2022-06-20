package com.example.acmovies.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.example.acmovies.model.Episode;

import java.util.List;

public class EpisodeRecyclerAdapter extends RecyclerView.Adapter<EpisodeRecyclerAdapter.EpsiodeViewHolder> {
    private Context context;
    private List<Episode> episodeList;
    private ItemClickListener itemOnClick;
    private int curentitem = 0;

    public EpisodeRecyclerAdapter(Context context, List<Episode> episodeList, ItemClickListener itemOnClick) {
        this.context = context;
        this.episodeList = episodeList;
        this.itemOnClick = itemOnClick;
    }

    @NonNull
    @Override
    public EpsiodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EpsiodeViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_episode_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EpsiodeViewHolder holder, int position) {
        Glide.with(context).load(episodeList.get(position).getImage().getImageUrl()).into(holder.img_ep);
        String episodeTitle = "";
        if (episodeList.get(position).getTitle() != null){
            episodeTitle = episodeList.get(position).getTitle();
        }
        holder.txt_episode.setText("Tập " + episodeList.get(position).getEpisode() + ": "+ episodeTitle);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curentitem = position;
                itemOnClick.onEpisodeClick(episodeList.get(position));
                notifyDataSetChanged();
            }
        });
        if(curentitem == position){
            holder.txt_episode.setEnabled(false);
            holder.txt_episode.setTextColor(Color.parseColor("#FF0000"));
        }
        else
        {
            holder.txt_episode.setEnabled(true);
            holder.txt_episode.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public class EpsiodeViewHolder extends RecyclerView.ViewHolder{
        ImageView img_ep;
        TextView txt_episode;
        public EpsiodeViewHolder(@NonNull View itemView) {
            super(itemView);
            img_ep = (ImageView) itemView.findViewById(R.id.img_ep);
            txt_episode = (TextView) itemView.findViewById(R.id.txt_ep_title);
        }
    }
}
