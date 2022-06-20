package com.example.acmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.R;
import com.example.acmovies.model.Actor;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActorRecyclerAdapter extends RecyclerView.Adapter<ActorRecyclerAdapter.ActorViewlHolder> {
    Context context;
    List<Actor> actorList;
    ItemClickListener itemClickListener;

    public ActorRecyclerAdapter(Context context, List<Actor> actorList, ItemClickListener itemClickListener) {
        this.context = context;
        this.actorList = actorList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ActorViewlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActorViewlHolder(LayoutInflater.from(context).inflate(R.layout.actor_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewlHolder holder, int position) {
        Glide.with(context).load(actorList.get(position).getImage().getImageUrl()).into(holder.imgActor);
    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }

    public class ActorViewlHolder extends RecyclerView.ViewHolder{
        CircleImageView imgActor;
        public ActorViewlHolder(@NonNull View itemView) {
            super(itemView);
            imgActor = (CircleImageView) itemView.findViewById(R.id.img_actor_Ava);
            imgActor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onActorClick(actorList.get(getAdapterPosition()));
                }
            });
        }
    }
}
