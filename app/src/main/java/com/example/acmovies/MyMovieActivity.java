package com.example.acmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.Interface.ItemDeleteClickListener;
import com.example.acmovies.adapter.HistoryRecyclerAdapter;
import com.example.acmovies.adapter.ItemRecyclerAdapter;
import com.example.acmovies.adapter.MovieListRecyclerAdapter;
import com.example.acmovies.adapter.MyMovieRecyclerAdapter;
import com.example.acmovies.fragments.SeriesMoviesFragment;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Pagination;
import com.example.acmovies.model.Status;
import com.example.acmovies.model.UserView;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMovieActivity extends AppCompatActivity implements ItemClickListener, ItemDeleteClickListener {
    private RecyclerView recyclerView;
    private ImageView btnBack;
    private ProgressBar progressLoadData;
    private MyMovieRecyclerAdapter myMovieRecyclerAdapter;
    private List<Movie> movieList = new ArrayList<>();

    private int user_id;
    private String token;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_movie);
        inView();
        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id", 0);
        token = intent.getStringExtra("token");
        getData();
    }

    private void inView(){
        btnBack = (ImageView) findViewById(R.id.btnBack);
        progressLoadData = (ProgressBar) findViewById(R.id.progres_loadData);
        btnBack.setOnClickListener(View -> onBackPressed());
        recyclerView = findViewById(R.id.recyclerMyMovie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myMovieRecyclerAdapter = new MyMovieRecyclerAdapter(this, movieList, this, this);
        recyclerView.setAdapter(myMovieRecyclerAdapter);
    }

    private void getData() {
        DataClient dataClient = APIUtils.getData();
        Call<List<Movie>> paginationCall = dataClient.GetMoivesbyUser("Bearer " + token, user_id);
        paginationCall.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()){
                    List<Movie> list = (List<Movie>) response.body();
                    for (Movie movie : list) {
                        movieList.add(movie);
                    }
                    myMovieRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("pagination", response.isSuccessful()+"loix");
                    getData();
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.d("pagination", t.toString() + "");
                getData();
            }
        });
    }

    @Override
    public void onMovieClick(Movie movie) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DataClient dataClient = APIUtils.getData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<Movie> callMovie = dataClient.GetMoviebyId(movie.getId());
                callMovie.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        Movie movie = response.body();
                        Intent intent = new Intent(MyMovieActivity.this, MovieDetailActivity.class);
                        intent.putExtra("movie",movie);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d("ERROR: ", t.getMessage().toString());
                    }
                });
            }
        }, 3000);
    }

    private void deleteUserMovie(Movie movie){
        DataClient dataClient = APIUtils.getData();
        Call<Status> callCheck = dataClient.DeleteUerMovie("Bearer " + token, user_id, movie.getId());
        callCheck.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus()){
                    movieList.remove(movie);
                    myMovieRecyclerAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Đã xóa khỏi danh sách phim", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("ERROR:", t.toString());
            }
        });
    }

    @Override
    public void onEpisodeClick(Episode episode) {

    }

    @Override
    public void onActorClick(Actor actor) {

    }

    @Override
    public void onGenreClick(Genres genres) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void deleteMovieClick(UserView userView) {

    }

    @Override
    public void deleteMovieClick(Movie movie) {
        deleteUserMovie(movie);
    }
}