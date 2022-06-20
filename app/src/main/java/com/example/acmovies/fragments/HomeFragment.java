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
import com.example.acmovies.MainActivity;
import com.example.acmovies.MovieDetailActivity;
import com.example.acmovies.R;
import com.example.acmovies.adapter.ItemRecyclerAdapter;
import com.example.acmovies.adapter.MainRecyclerAdapter;
import com.example.acmovies.adapter.SliderPagerAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.ListMovie;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Movie;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
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
    private List<Genres> hotGenresList;
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
        gethotMovies();
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

    private void moviesViewTab(List<Genres> list)
    {
        for (int i = 0; i<list.size(); i++)
        {
            tab_action.addTab(tab_action.newTab().setText(list.get(i).getName()));
        }
        tab_action.setTabGravity(TabLayout.GRAVITY_FILL);
        tab_action.setTabTextColors(Color.WHITE,Color.YELLOW);
        tab_action.setSelectedTabIndicatorColor(Color.YELLOW);

        getMoviesbyGenre(list.get(tab_action.getSelectedTabPosition()).getId());
        tab_action.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                getMoviesbyGenre(list.get(tab.getPosition()).getId());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

//   get hot movies
    private void getnewMovies()
    {
        progressBar.setVisibility(View.VISIBLE);

        Call<List<Movie>> callnewMovies = dataClient.GetNewMovie();
        callnewMovies.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    ArrayList<Movie> list = (ArrayList<Movie>) response.body();
                    for (Movie movie : list)
                    {
                        newMovieList.add(movie);
                    }
                    sliderPagerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("paginationHome ", response.isSuccessful()+"");
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
                getnewMovies();
            }
        });

    }

//  Get hot movies
    private void gethotMovies(){
        Call<List<ListMovie>> callhotMovies = dataClient.GetHotMovie();
        callhotMovies.enqueue(new Callback<List<ListMovie>>() {
            @Override
            public void onResponse(Call<List<ListMovie>> call, Response<List<ListMovie>> response) {
                if(response.isSuccessful()){
                    ArrayList<ListMovie> arr_cats = (ArrayList<ListMovie>) response.body();
                    for (ListMovie listMovie : arr_cats)
                    {
                        listhotMovie.add(listMovie);
                    }
                    mainRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("paginationHome ", response.isSuccessful()+"");
                }
            }

            @Override
            public void onFailure(Call<List<ListMovie>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
                gethotMovies();
            }
        });
    }

//  Get hot Genres
    private void gethotGenres(){
        Call<List<Genres>> callhotGenres = dataClient.GetHotGenre();
        callhotGenres.enqueue(new Callback<List<Genres>>() {
            @Override
            public void onResponse(Call<List<Genres>> call, Response<List<Genres>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Genres> arr_genres = (ArrayList<Genres>) response.body();
                    for (Genres genres : arr_genres) {
                        hotGenresList.add(genres);
                    }
                    moviesViewTab(hotGenresList);
                } else {
                    Log.d("paginationHome ", response.isSuccessful()+"");
                }
            }

            @Override
            public void onFailure(Call<List<Genres>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
                gethotGenres();
            }
        });
    }

//  Get Movies by the Genre
    private void getMoviesbyGenre(Integer id)
    {
        DataClient dataClient = APIUtils.getData();
        Call<List<Movie>> callmoviesbygenre = dataClient.GetMoviesbyGenre(id);
        callmoviesbygenre.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                ArrayList<Movie> list = (ArrayList<Movie>) response.body();
                setTab_recycler(list);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
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
    public void onMovieClick(Movie movie)
    {
        sendtoMovieDetail(movie.getId());
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
                Call<Movie> callMovie = dataClient.GetMoviebyId(id);
                callMovie.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        Movie movie = response.body();
                        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
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
