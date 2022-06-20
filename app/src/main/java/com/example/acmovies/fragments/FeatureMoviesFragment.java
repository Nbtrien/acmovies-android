package com.example.acmovies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.MovieDetailActivity;
import com.example.acmovies.R;
import com.example.acmovies.adapter.ItemRecyclerAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Pagination;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeatureMoviesFragment extends Fragment implements ItemClickListener {
    private View view;
    private String category="phim lẻ";
    private RecyclerView moviesRecycler;
    private ProgressBar progressLoadData;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    private List<Movie> movieList;
    private boolean mInstanceAlreadySaved;
    private Bundle mSavedOutState;
    private int currentPage = 1;
    private int totalAvailablePages = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_movie,container,false);

        inView();
        if (null == savedInstanceState && null != mSavedOutState) {
            savedInstanceState = mSavedOutState;
            List<Movie> movieList1 = (List<Movie>) savedInstanceState.getSerializable("movies");
            for (Movie movie : movieList1){
                movieList.add(movie);
            }
            itemRecyclerAdapter.notifyDataSetChanged();
        } else {
            getData();
        }

        mInstanceAlreadySaved = false;
        return view;
    }

    private void inView() {
        progressLoadData = (ProgressBar) view.findViewById(R.id.progres_loadData);
        moviesRecycler = (RecyclerView) view.findViewById(R.id.movieList_Recycler);
        moviesRecycler.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        moviesRecycler.setLayoutManager(mLayoutManager);
        moviesRecycler.addItemDecoration(new SeriesMoviesFragment.SpacesItemDecoration(40));

        movieList = new ArrayList<>();
        itemRecyclerAdapter = new ItemRecyclerAdapter(getContext(), movieList, this);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("movies", (Serializable) movieList);
        mInstanceAlreadySaved = true;
    }

    private void getData() {
        toggleLoading();
        DataClient dataClient = APIUtils.getData();
        Call<Pagination> paginationCall = dataClient.GetMoviesbyCategory(category, currentPage);
        paginationCall.enqueue(new Callback<Pagination>() {
            @Override
            public void onResponse(Call<Pagination> call, Response<Pagination> response) {
                if(response.isSuccessful()){
                    toggleLoading();
                    Pagination pag = response.body();
                        totalAvailablePages = pag.getLastPage();
                        for (Movie movie : pag.getMovies()){
                            movieList.add(movie);
                        }
                        itemRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("paginationfeature", response.isSuccessful()+"loix");
                    toggleLoading();
                    getData();
                }

            }

            @Override
            public void onFailure(Call<Pagination> call, Throwable t) {
                Log.d("paginationfeature", t.toString()+"");
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
        progressDialog = new ProgressDialog(getContext());
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
                        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
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
    public void onStop()
    {
        if (!mInstanceAlreadySaved)
        {
            mSavedOutState = new Bundle();
            onSaveInstanceState( mSavedOutState );
        }

        super.onStop();
    }
}
