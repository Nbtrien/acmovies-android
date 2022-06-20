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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.MovieDetailActivity;
import com.example.acmovies.MovieListActivity;
import com.example.acmovies.R;
import com.example.acmovies.adapter.GenreRecyclerAdapter;
import com.example.acmovies.adapter.MainRecyclerAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.ListMovie;
import com.example.acmovies.model.Movie;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieCollectionFragment extends Fragment implements ItemClickListener {
    private View view;
    private ProgressBar progressLoaddata;
    private RecyclerView genrerecyclerView, seriesrecyclerView;
    private GenreRecyclerAdapter recyclerAdapter;
    private MainRecyclerAdapter mainRecyclerAdapter;
    private List<Genres> genresList;
    private List<ListMovie> movieList;
    private boolean mInstanceAlreadySaved;
    private Bundle mSavedOutState;
    private DataClient dataClient = APIUtils.getData();;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie_collection,container,false);

        inView();
        if (null == savedInstanceState && null != mSavedOutState) {
            savedInstanceState = mSavedOutState;
            List<Genres> genresList1 = (List<Genres>) savedInstanceState.getSerializable("genres");
            for (Genres genres : genresList1){
                genresList.add(genres);
            }

            List<ListMovie> movieList1 = (List<ListMovie>) savedInstanceState.getSerializable("movies");
            for (ListMovie movie : movieList1){
                movieList.add(movie);
            }
            recyclerAdapter.notifyDataSetChanged();
            mainRecyclerAdapter.notifyDataSetChanged();
        } else {
            getGenres();
            getSeries();
        }

        mInstanceAlreadySaved = false;
        return view;
    }

    private void inView()
    {
        progressLoaddata = (ProgressBar) view.findViewById(R.id.progres_loadData1);
        genrerecyclerView = (RecyclerView) view.findViewById(R.id.genreRecyclerView);
        genresList = new ArrayList<>();
        recyclerAdapter = new GenreRecyclerAdapter(getContext(), genresList, this);
        genrerecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, LinearLayoutManager.HORIZONTAL,false);
        genrerecyclerView.setLayoutManager(gridLayoutManager);
        genrerecyclerView.setAdapter(recyclerAdapter);

        seriesrecyclerView = (RecyclerView) view.findViewById(R.id.seriesrecyclerView);
        movieList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        seriesrecyclerView.setLayoutManager(layoutManager);
        seriesrecyclerView.setHasFixedSize(true);
        mainRecyclerAdapter = new MainRecyclerAdapter(getContext(), movieList);
        seriesrecyclerView.setAdapter(mainRecyclerAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("movies", (Serializable) movieList);
        outState.putSerializable("genres", (Serializable) genresList);
        mInstanceAlreadySaved = true;
    }

//  Get genres
    private void getGenres()
    {
        progressLoaddata.setVisibility(View.VISIBLE);
        Call<List<Genres>> callAllGenre = dataClient.GetAllGenre();
        callAllGenre.enqueue(new Callback<List<Genres>>() {
            @Override
            public void onResponse(Call<List<Genres>> call, Response<List<Genres>> response) {
                progressLoaddata.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    ArrayList<Genres> list = (ArrayList<Genres>) response.body();
                    for (Genres genre : list){
                        genresList.add(genre);
                    }
                    recyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("paginationcollect", response.isSuccessful()+"loix");
                }
            }

            @Override
            public void onFailure(Call<List<Genres>> call, Throwable t) {
                Log.d("paginationcollect: ", t.getMessage().toString());
                getGenres();
            }
        });
    }

//  Get series
    private void getSeries(){
        Call<List<ListMovie>> callhotMovies = dataClient.GetAllSeries();
        callhotMovies.enqueue(new Callback<List<ListMovie>>() {
            @Override
            public void onResponse(Call<List<ListMovie>> call, Response<List<ListMovie>> response) {
                progressLoaddata.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    ArrayList<ListMovie> arr_cats = (ArrayList<ListMovie>) response.body();
                    for (ListMovie listMovie : arr_cats) {
                        movieList.add(listMovie);
                    }
                    mainRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("paginationcollect", response.isSuccessful()+"loix");
                }
            }

            @Override
            public void onFailure(Call<List<ListMovie>> call, Throwable t) {
                Log.d("paginationcollect: ", t.getMessage().toString());
                getSeries();
            }
        });
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

    @Override
    public void onMovieClick(Movie movie) {

    }

    @Override
    public void onEpisodeClick(Episode episode) {

    }

    @Override
    public void onActorClick(Actor actor) {

    }

    @Override
    public void onGenreClick(Genres genres) {
        Intent intent = new Intent(getActivity(), MovieListActivity.class);
        intent.putExtra("genre", genres.getName());
        intent.putExtra("genre_id", genres.getId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
