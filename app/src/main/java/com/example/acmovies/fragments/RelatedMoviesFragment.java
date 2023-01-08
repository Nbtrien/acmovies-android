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
import com.example.acmovies.model.Genre;
import com.example.acmovies.model.ListMovie;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.WrapperData;
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

        getSimilarMovie();
        getSameSeries();
        return view;
    }

    private void inView()
    {
        mainRecycler = view.findViewById(R.id.related_recycler);
    }

//    Get Related Movies
    private void getSimilarMovie()
    {
        DataClient dataClient = APIUtils.getData();
        Call<WrapperData<Movie>> call = dataClient.getSimilaMovie(movie_id);
        call.enqueue(new Callback<WrapperData<Movie>>() {
            @Override
            public void onResponse(Call<WrapperData<Movie>> call, Response<WrapperData<Movie>> response) {
                if (response.isSuccessful())
                {
                    ArrayList<Movie> movies = (ArrayList<Movie>) response.body().getData();
                    ListMovie listMovie = new ListMovie();
                    listMovie.setTitle("Phim lien quan");
                    listMovie.setMovies(movies);
                    listRelatedMovie.add(listMovie);
                    mainRecyclerAdapter.notifyDataSetChanged();
                    Log.d("RelatedMoviesFragment", listMovie.toString());
                }   else {
                    Log.d("RelatedMoviesFragment", "onResponse: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<WrapperData<Movie>> call, Throwable t) {
                getSimilarMovie();
            }
        });
    }

    private void getSameSeries(){
        DataClient dataClient = APIUtils.getData();
        Call<WrapperData<ListMovie>> callLM = dataClient.getSameSiries(movie_id);
        callLM.enqueue(new Callback<WrapperData<ListMovie>>() {
            @Override
            public void onResponse(Call<WrapperData<ListMovie>> call, Response<WrapperData<ListMovie>> response) {
                if (response.isSuccessful())
                {
                    ArrayList<ListMovie> listMovies = (ArrayList<ListMovie>) response.body().getData();
                    for (ListMovie list : listMovies) {
                        listRelatedMovie.add(list);
                    }
                    mainRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("TAG", "onResponse: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<WrapperData<ListMovie>> call, Throwable t) {
                getSameSeries();
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
    public void onGenreClick(Genre genres) {

    }
}
