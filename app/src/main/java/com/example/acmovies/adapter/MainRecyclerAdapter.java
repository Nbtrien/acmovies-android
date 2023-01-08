package com.example.acmovies.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.MovieDetailActivity;
import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.R;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Genre;
import com.example.acmovies.model.ListMovie;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Video;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> implements ItemClickListener {

    Context context;
    List<ListMovie> listMovieList;

    public MainRecyclerAdapter(Context context, List<ListMovie> listMovieList) {
        this.context = context;
        this.listMovieList = listMovieList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position)
    {
        holder.txtCategories.setText(listMovieList.get(position).getTitle().substring(0, 1).toUpperCase() + listMovieList.get(position).getTitle().substring(1));
//        if ((listMovieList.get(position).getMovies()) != null)
        setItemRecycler(holder.recyclerView, listMovieList.get(position).getMovies());
    }

    @Override
    public int getItemCount() {
        return listMovieList.size();
    }

    @Override
    public void onMovieClick(Movie movie)
    {
        sendtoMovieDetail(movie.getId());
    }

    @Override
    public void onEpisodeClick(Episode episode) {

    }

    @Override
    public void onActorClick(Actor actor) {

    }

    @Override
    public void onGenreClick(Genre genres) {

    }

    private void sendtoMovieDetail(Integer id)
    {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);
        DataClient dataClient = APIUtils.getData();
        Call<Movie> callMovie = dataClient.getMovie(id);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callMovie.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        Movie movie = response.body();
                        Call<Video> callVideo = dataClient.getVideobyMovie(movie.getId());
                        callVideo.enqueue(new Callback<Video>() {
                            @Override
                            public void onResponse(Call<Video> call, Response<Video> response) {
                                Video video = response.body();
                                Intent intent = new Intent(context, MovieDetailActivity.class);
                                intent.putExtra("movie",movie);
                                intent.putExtra("video",video);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Video> call, Throwable t) {
                                Log.d("moviebla", t.getMessage().toString());
                            }
                        });
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d("moviebla", t.getMessage().toString());
                    }
                });
            }
        }, 3000);
    }


    public class MainViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtCategories;
        RecyclerView recyclerView;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategories = (TextView) itemView.findViewById(R.id.txt_item_cat);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler);
        }
    }

    private void setItemRecycler(RecyclerView recycler, List<Movie> list)
    {
        ItemRecyclerAdapter adapter = new ItemRecyclerAdapter(context,list,this);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        recycler.setAdapter(adapter);
    }
}
