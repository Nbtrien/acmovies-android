package com.example.acmovies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import com.example.acmovies.model.Genre;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Video;
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

public class SeriesMoviesFragment extends Fragment implements ItemClickListener {
    private View view;
    private RecyclerView moviesRecycler;
    private String category="phim bộ";
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
        view = inflater.inflate(R.layout.fragment_list_movie, container, false);

        inView();
        if (null == savedInstanceState && null != mSavedOutState) {
            savedInstanceState = mSavedOutState;
            List<Movie> movieList1 = (List<Movie>) savedInstanceState.getSerializable("movies");
            for (Movie movie : movieList1){
                movieList.add(movie);
            }
            itemRecyclerAdapter.notifyDataSetChanged();
        } else {
            getMovies();
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
                        getMovies();
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

    private void getMovies() {
        toggleLoading();
        Map<String, String> params = new HashMap<>();
        params.put("keyword", "releasedate");
        params.put("limit", "9");
        params.put("category", category);
        params.put("page",  Integer.toString(currentPage) );
        DataClient dataClient = APIUtils.getData();
        Call<WrapperData<Movie>> moviesCall = dataClient.getMovies(params);
        moviesCall.enqueue(new Callback<WrapperData<Movie>>() {
            @Override
            public void onResponse(Call<WrapperData<Movie>> call, Response<WrapperData<Movie>> response) {
                if(response.isSuccessful()){
                    toggleLoading();
                    ArrayList<Movie> list = (ArrayList<Movie>) response.body().getData();

                    totalAvailablePages = response.body().getMeta().getLastPage();
                    for (Movie movie : list){
                        movieList.add(movie);
                    }
                    itemRecyclerAdapter.notifyDataSetChanged();
                    Log.d("paginationseries", response.body().getMeta().toString());
                } else {
                    Log.d("paginationseries", response.isSuccessful()+"loix");
                    toggleLoading();
                }
            }

            @Override
            public void onFailure(Call<WrapperData<Movie>> call, Throwable t) {
                Log.d("paginationseries", t.toString() + "");
                toggleLoading();
                getMovies();
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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DataClient dataClient = APIUtils.getData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Call<Movie> callMovie = dataClient.getMovie(id);
                callMovie.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        Movie movie = response.body();
                        Call<Video> callVideo = dataClient.getVideobyMovie(movie.getId());
                        callVideo.enqueue(new Callback<Video>() {
                            @Override
                            public void onResponse(Call<Video> call, Response<Video> response) {
                                Video video = response.body();
                                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                                intent.putExtra("movie",movie);
                                intent.putExtra("video",video);
                                startActivity(intent);
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

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
            outRect.top = space;
        }
    }

}
