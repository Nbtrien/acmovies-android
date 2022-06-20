package com.example.acmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.adapter.ItemRecyclerAdapter;
import com.example.acmovies.fragments.SeriesMoviesFragment;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Pagination;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements ItemClickListener {
    private ImageView btnBack;
    private TextView txtgenreTitle;
    private RecyclerView moviesRecycler;
    private ProgressBar progressLoadData;

    private ItemRecyclerAdapter itemRecyclerAdapter;
    private List<Movie> movieList;

    private String genreTitle;
    private int genre_id;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);
        inView();

        Intent intent = getIntent();
        genreTitle = intent.getStringExtra("genre");
        genre_id = intent.getIntExtra("genre_id", 0);
        txtgenreTitle.setText("Phim "+genreTitle);

        getData();

    }

    private void inView() {
        btnBack = (ImageView) findViewById(R.id.btn_back);
        txtgenreTitle = (TextView) findViewById(R.id.txt_genre_title);
        moviesRecycler = (RecyclerView) findViewById(R.id.movieList_Recycler);
        progressLoadData = (ProgressBar) findViewById(R.id.progres_loadData);

        btnBack.setOnClickListener(View -> onBackPressed());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MovieListActivity.this, 3);
        moviesRecycler.setLayoutManager(mLayoutManager);
        moviesRecycler.addItemDecoration(new SeriesMoviesFragment.SpacesItemDecoration(40));

        movieList = new ArrayList<>();
        itemRecyclerAdapter = new ItemRecyclerAdapter(MovieListActivity.this, movieList, this);
        moviesRecycler.setAdapter(itemRecyclerAdapter);

        moviesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(moviesRecycler.canScrollVertically(1)){
                    if (currentPage <= totalAvailablePages){
                        currentPage += 1;
                        getData();
                    }
                }
            }
        });
    }

    private void getData() {
        toggleLoading();
        DataClient dataClient = APIUtils.getData();
        Call<Pagination> paginationCall = dataClient.GetAllMoviesbyGenre(genre_id, currentPage);
        paginationCall.enqueue(new Callback<Pagination>() {
            @Override
            public void onResponse(Call<Pagination> call, Response<Pagination> response) {
                if(response.isSuccessful()){
                    toggleLoading();
                    Pagination pag = response.body();
                    totalAvailablePages = pag.getLastPage();
                    for (Movie movie : pag.getMovies()) {
                        movieList.add(movie);
                    }
                    itemRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("pagination", response.isSuccessful()+"loix");
                    toggleLoading();
                    getData();
                }
            }

            @Override
            public void onFailure(Call<Pagination> call, Throwable t) {
                Log.d("pagination", t.toString() + "");
                toggleLoading();
                getData();
            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1){
            if (progressLoadData.getVisibility() != View.GONE) {
                progressLoadData.setVisibility(View.GONE);
            } else {
                progressLoadData.setVisibility(View.VISIBLE);
            }
        }
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
                        Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
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

}