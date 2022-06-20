package com.example.acmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.R;
import com.example.acmovies.model.Comment;

import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentViewholder> {
    Context context;
    List<Comment> commentList;

    public CommentRecyclerAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentRecyclerAdapter.CommentViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewholder(LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.CommentViewholder holder, int position) {
        String username = commentList.get(position).getUser().getName();
        String name = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        String fname = String.valueOf(username.charAt(0));

        holder.txtUserName.setText(name);
        holder.txtContent.setText(commentList.get(position).getContent());
        holder.txtTimeComment.setText(commentList.get(position).getTime());
        holder.txtFirstname.setText(fname);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewholder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtContent, txtTimeComment, txtFirstname;
        public CommentViewholder(@NonNull View itemView) {
            super(itemView);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            txtTimeComment = (TextView) itemView.findViewById(R.id.txtTimeComment);
            txtFirstname = (TextView) itemView.findViewById(R.id.txtFirstname);
        }
    }
}
