package com.example.acmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.adapter.SearchRecyclerAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Pagination;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements ItemClickListener {
    private ImageView btnSearch, btnBack;
    private RecyclerView searchrecycler;
    private EditText inputSearch;
    private TextView result;
    private ProgressBar progressLoaddata;
    private SearchRecyclerAdapter recyclerAdapter;
    private List<Movie> movieList;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        inView();
        btnBack.setOnClickListener(View -> onBackPressed());
        searchrecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchrecycler.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_divider);
        itemDecoration.setDrawable(drawable);
        searchrecycler.addItemDecoration(itemDecoration);
        searchrecycler.setAdapter(recyclerAdapter);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null){
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                result.setVisibility(View.GONE);
                progressLoaddata.setVisibility(View.VISIBLE);
                if (!editable.toString().trim().isEmpty()){
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    movieList.clear();
                                    recyclerAdapter.notifyDataSetChanged();
                                    currentPage = 1;
                                    totalAvailablePages = 1;
                                    searchMovie(editable.toString());
                                }
                            });

                        }
                    }, 800);
                } else {
                    movieList.clear();
                    recyclerAdapter.notifyDataSetChanged();
                    progressLoaddata.setVisibility(View.GONE);
                    result.setVisibility(View.GONE);
                }
            }
        });
        searchrecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(searchrecycler.canScrollVertically(1)){
                    if(!inputSearch.getText().toString().isEmpty()){
                        if (currentPage < totalAvailablePages){
                            currentPage += 1;
                            searchMovie(inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        inputSearch.requestFocus();
    }

    private void inView(){
        progressLoaddata = (ProgressBar) findViewById(R.id.search_loadData);
        btnSearch = (ImageView) findViewById(R.id.btn_Search);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        searchrecycler = (RecyclerView) findViewById(R.id.moviesSearchRecyclerView);
        movieList = new ArrayList<>();
        recyclerAdapter = new SearchRecyclerAdapter(this, movieList, this);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        result = (TextView) findViewById(R.id.txt_result);
        result.setVisibility(View.GONE);
    }

    private void searchMovie(String key){
        DataClient dataClient = APIUtils.getData();
        Call<Pagination> callSearch = dataClient.SearchMovie(key, currentPage);
        callSearch.enqueue(new Callback<Pagination>() {
            @Override
            public void onResponse(Call<Pagination> call, Response<Pagination> response) {
                progressLoaddata.setVisibility(View.GONE);
                Pagination pag = response.body();
                totalAvailablePages = pag.getLastPage();
                Log.d("pagination", pag.getMovies().size()+"");
                if (pag.getMovies().size() != 0){
                    for (Movie movie : pag.getMovies()){
                        movieList.add(movie);
                    }
                    recyclerAdapter.notifyDataSetChanged();
                }
                if (movieList.size() == 0) {
                    result.setVisibility(View.VISIBLE);
                } else {
                    result.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Pagination> call, Throwable t) {
                Log.d("pagination", t.toString()+"");
            }
        });
    }

    @Override
    public void onMovieClick(Movie movie) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SearchActivity.this);
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
                        Intent intent = new Intent(SearchActivity.this, MovieDetailActivity.class);
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