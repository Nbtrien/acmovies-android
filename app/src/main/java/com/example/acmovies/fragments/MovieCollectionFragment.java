package com.example.acmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.MovieListActivity;
import com.example.acmovies.R;
import com.example.acmovies.adapter.GenreRecyclerAdapter;
import com.example.acmovies.adapter.MainRecyclerAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genre;
import com.example.acmovies.model.ListMovie;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Series;
import com.example.acmovies.model.WrapperData;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieCollectionFragment extends Fragment implements ItemClickListener {
    private View view;
    private ProgressBar progressLoaddata;
    private RecyclerView genrerecyclerView, seriesrecyclerView;
    private GenreRecyclerAdapter recyclerAdapter;
    private MainRecyclerAdapter mainRecyclerAdapter;
    private List<Genre> genresList;
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
            List<Genre> genresList1 = (List<Genre>) savedInstanceState.getSerializable("genres");
            for (Genre genres : genresList1){
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
        Map<String, String> params = new HashMap<>();
        params.put("limit", "0");
        Call<WrapperData<Genre>> call = dataClient.getGenres(params);
        call.enqueue(new Callback<WrapperData<Genre>>() {
            @Override
            public void onResponse(Call<WrapperData<Genre>> call, Response<WrapperData<Genre>> response) {
                progressLoaddata.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    ArrayList<Genre> list = (ArrayList<Genre>) response.body().getData();
                    for (Genre genre : list){
                        genresList.add(genre);
                    }
                    recyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("paginationcollect", response.isSuccessful()+"loix");
                }
            }

            @Override
            public void onFailure(Call<WrapperData<Genre>> call, Throwable t) {

            }
        });
    }

//  Get series
    private void getSeries(){
        Map<String, String> params = new HashMap<>();
        Call<WrapperData<Series>> callSeries = dataClient.getSeries(params);
        callSeries.enqueue(new Callback<WrapperData<Series>>() {
            @Override
            public void onResponse(Call<WrapperData<Series>> call, Response<WrapperData<Series>> response) {
                if (response.isSuccessful()){
                    ArrayList<Series> seriesList = (ArrayList<Series>) response.body().getData();
                    for (Series series : seriesList) {
                        Call<WrapperData<Movie>> call1Movies = dataClient.getMoviesbySeries(series.getId());
                        call1Movies.enqueue(new Callback<WrapperData<Movie>>() {
                            @Override
                            public void onResponse(Call<WrapperData<Movie>> call, Response<WrapperData<Movie>> response) {
                                if(response.isSuccessful()){
                                    ArrayList<Movie> movies = (ArrayList<Movie>) response.body().getData();
                                    ListMovie listMovie = new ListMovie();
                                    listMovie.setTitle(series.getName());
                                    listMovie.setMovies(movies);
                                    movieList.add(listMovie);
                                    mainRecyclerAdapter.notifyDataSetChanged();
                                } else {

                                }
                            }

                            @Override
                            public void onFailure(Call<WrapperData<Movie>> call, Throwable t) {

                            }
                        });
                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<WrapperData<Series>> call, Throwable t) {

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
    public void onGenreClick(Genre genres) {
        Intent intent = new Intent(getActivity(), MovieListActivity.class);
        intent.putExtra("genre", genres.getName());
        intent.putExtra("genre_id", genres.getId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
