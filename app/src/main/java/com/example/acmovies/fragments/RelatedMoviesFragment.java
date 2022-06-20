package com.example.acmovies.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.R;
import com.example.acmovies.adapter.MainRecyclerAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.ListMovie;
import com.example.acmovies.model.Movie;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelatedMoviesFragment extends Fragment implements ItemClickListener {
    private View view;
    private RecyclerView mainRecycler;
    private Integer movie_id;
    private MainRecyclerAdapter mainRecyclerAdapter;
    private List<ListMovie> listRelatedMovie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie_related, container, false);
        inView();

        Bundle bundle = getArguments();
        movie_id = bundle.getInt("movie_id");

        listRelatedMovie = new ArrayList<>();
        mainRecyclerAdapter = new MainRecyclerAdapter(getContext(), listRelatedMovie);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mainRecycler.setLayoutManager(layoutManager);
        mainRecycler.setHasFixedSize(true);
        mainRecycler.setAdapter(mainRecyclerAdapter);

        getData();
        return view;
    }

    private void inView()
    {
        mainRecycler = view.findViewById(R.id.related_recycler);
    }

//    Get Related Movies
    private void getData()
    {
        DataClient dataClient = APIUtils.getData();
        Call<List<ListMovie>> callrelatedMovies = dataClient.GetRelatedMovies(movie_id);
        callrelatedMovies.enqueue(new Callback<List<ListMovie>>() {
            @Override
            public void onResponse(Call<List<ListMovie>> call, Response<List<ListMovie>> response) {
                if (response.isSuccessful()){
                    ArrayList<ListMovie> list = (ArrayList<ListMovie>) response.body();
                    for (ListMovie movie : list)
                    {
                        listRelatedMovie.add(movie);
                    }
                    mainRecyclerAdapter.notifyDataSetChanged();
                } else {
                    getData();
                }

            }

            @Override
            public void onFailure(Call<List<ListMovie>> call, Throwable t) {
                Log.d("RELATED: ", t.getMessage().toString());
                getData();
            }
        });
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

    }
}
