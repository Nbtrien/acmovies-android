package com.example.acmovies.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.MovieDetailActivity;
import com.example.acmovies.R;
import com.example.acmovies.adapter.ItemRecyclerAdapter;
import com.example.acmovies.adapter.MainRecyclerAdapter;
import com.example.acmovies.adapter.SliderPagerAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Video;
import com.example.acmovies.model.WrapperData;
import com.example.acmovies.model.Genre;
import com.example.acmovies.model.ListMovie;
import com.example.acmovies.model.Episode;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements ItemClickListener {
    private View view;
    private ViewPager slidevPager;
    private TabLayout indicatortbLayout;
    private RecyclerView main_recycler, tab_recycler;
    private TabLayout tab_action;
    private ProgressBar progressBar;

    private SliderPagerAdapter sliderPagerAdapter;
    private ItemRecyclerAdapter itemRecyclerAdapter;
    private MainRecyclerAdapter mainRecyclerAdapter;
    private List<ListMovie> listhotMovie;
    private List<Genre> hotGenresList;
    private List<Movie> newMovieList;

    private Toolbar toolbar;
    private DataClient dataClient = APIUtils.getData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        inView();

        newMovieList = new ArrayList<>();
        sliderPagerAdapter = new SliderPagerAdapter(getContext(), newMovieList,this);
        slidevPager.setAdapter(sliderPagerAdapter);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        listhotMovie = new ArrayList<>();
        setMain_recycler(listhotMovie);

        hotGenresList = new ArrayList<>();

        getnewMovies();
        getHotFeaMovies();
        getHotSeriMovies();
        gethotGenres();
        return view;
    }

    private void inView()
    {
        progressBar = (ProgressBar) view.findViewById(R.id.progres_loadData);
        slidevPager = (ViewPager) view.findViewById(R.id.silder_pager);
        main_recycler = (RecyclerView) view.findViewById(R.id.main_recycler);
        tab_recycler = (RecyclerView) view.findViewById(R.id.recycler_tab);
        tab_action = (TabLayout) view.findViewById(R.id.tab_action);
    }

    private void moviesViewTab(List<Genre> list)
    {
        for (int i = 0; i<list.size(); i++)
        {
            String genresName = list.get(i).getName().substring(0, 1).toUpperCase() + list.get(i).getName().substring(1);
            tab_action.addTab(tab_action.newTab().setText(genresName));
        }
        tab_action.setTabGravity(TabLayout.GRAVITY_FILL);
        tab_action.setTabTextColors(Color.WHITE,Color.YELLOW);
        tab_action.setSelectedTabIndicatorColor(Color.YELLOW);

//        getMoviesbyGenre(list.get(tab_action.getSelectedTabPosition()).getId());
        getMoviesbyGenre(list.get(tab_action.getSelectedTabPosition()).getName());
        tab_action.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
//                getMoviesbyGenre(list.get(tab.getPosition()).getId());
                getMoviesbyGenre(list.get(tab.getPosition()).getName());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

//   get new movies
    private void getnewMovies()
    {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("keyword", "created_at");
        params.put("limit", "10");
        Call<WrapperData<Movie>> callnewMovies = dataClient.getMovies(params);
        callnewMovies.enqueue(new Callback<WrapperData<Movie>>() {
            @Override
            public void onResponse(Call<WrapperData<Movie>> call, Response<WrapperData<Movie>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    ArrayList<Movie> list = (ArrayList<Movie>) response.body().getData();
                    for (Movie movie : list)
                    {
                        newMovieList.add(movie);
                    }
                    sliderPagerAdapter.notifyDataSetChanged();
                    Log.d("newmovies ", response.body().getData()+"");
                } else {
                    Log.d("paginationHome ", response.isSuccessful()+"");
                }
            }

            @Override
            public void onFailure(Call<WrapperData<Movie>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
                getnewMovies();
            }
        });
    }
//  Get hot feature movies
    private void getHotFeaMovies(){
        Map<String, String> params = new HashMap<>();
        params.put("keyword", "view");
        params.put("limit", "10");
        params.put("category", "phim lẻ");
        Call<WrapperData<Movie>> callhotMovies = dataClient.getMovies(params);
        callhotMovies.enqueue(new Callback<WrapperData<Movie>>() {
            @Override
            public void onResponse(Call<WrapperData<Movie>> call, Response<WrapperData<Movie>> response) {
                if(response.isSuccessful()){
                    ArrayList<Movie> list = (ArrayList<Movie>) response.body().getData();
                    ListMovie lsMovie = new ListMovie();
                    lsMovie.setTitle("Phim lẻ hot");
                    lsMovie.setMovies(list);
                    listhotMovie.add(lsMovie);
                    mainRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("GetHotMovies ", response.isSuccessful()+"");
                }
            }

            @Override
            public void onFailure(Call<WrapperData<Movie>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
                getHotFeaMovies();
            }
        });
    }

//  Get hot series movies
    private void getHotSeriMovies(){
        Map<String, String> params = new HashMap<>();
        params.put("keyword", "view");
        params.put("limit", "10");
        params.put("category", "phim bộ");
        Call<WrapperData<Movie>> callhotMovies = dataClient.getMovies(params);
        callhotMovies.enqueue(new Callback<WrapperData<Movie>>() {
            @Override
            public void onResponse(Call<WrapperData<Movie>> call, Response<WrapperData<Movie>> response) {
                if(response.isSuccessful()){
                    ArrayList<Movie> list = (ArrayList<Movie>) response.body().getData();
                    ListMovie lsMovie = new ListMovie();
                    lsMovie.setTitle("Phim bộ hot");
                    lsMovie.setMovies(list);
                    listhotMovie.add(lsMovie);
                    mainRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("GetHotMovies ", response.isSuccessful()+"");
                }
            }

            @Override
            public void onFailure(Call<WrapperData<Movie>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
                getHotSeriMovies();
            }
        });
    }

//  Get hot Genres
    private void gethotGenres(){
        Call<WrapperData<Genre>> callhotGenres = dataClient.getGenres(12);
        callhotGenres.enqueue(new Callback<WrapperData<Genre>>() {
            @Override
            public void onResponse(Call<WrapperData<Genre>> call, Response<WrapperData<Genre>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Genre> arr_genres = (ArrayList<Genre>) response.body().getData();
                    for (Genre genres : arr_genres) {
                        hotGenresList.add(genres);
                    }
                    moviesViewTab(hotGenresList);
                } else {
                    Log.d("paginationHome ", response.isSuccessful()+"");
                }
            }

            @Override
            public void onFailure(Call<WrapperData<Genre>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
                gethotGenres();
            }
        });
    }

//  Get Movies by the Genre
    private void getMoviesbyGenre(String genre)
    {
        DataClient dataClient = APIUtils.getData();
        Map<String, String> params = new HashMap<>();
        params.put("genre", genre);
        Call<WrapperData<Movie>> callmoviesbygenre = dataClient.getMovies(params);
        callmoviesbygenre.enqueue(new Callback<WrapperData<Movie>>() {
            @Override
            public void onResponse(Call<WrapperData<Movie>> call, Response<WrapperData<Movie>> response) {
                ArrayList<Movie> list = (ArrayList<Movie>) response.body().getData();
                setTab_recycler(list);
            }

            @Override
            public void onFailure(Call<WrapperData<Movie>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
            }
        });
    }

    private void setMain_recycler(List<ListMovie> list)
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        main_recycler.setLayoutManager(layoutManager);
        main_recycler.setHasFixedSize(true);
        mainRecyclerAdapter = new MainRecyclerAdapter(getContext(), list);
        main_recycler.setAdapter(mainRecyclerAdapter);
    }

    private void setTab_recycler(List<Movie> list)
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        tab_recycler.setLayoutManager(layoutManager);
        tab_recycler.setHasFixedSize(true);
        itemRecyclerAdapter = new ItemRecyclerAdapter(getContext(), list, this);
        tab_recycler.setAdapter(itemRecyclerAdapter);
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

    class SliderTimer extends TimerTask
    {
        @Override
        public void run() {
            if (isAdded()) getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (slidevPager.getCurrentItem()< newMovieList.size()-1)
                        {
                            slidevPager.setCurrentItem(slidevPager.getCurrentItem()+1);
                        }
                        else
                            slidevPager.setCurrentItem(0);
                    }
                });
        }
    }
}
