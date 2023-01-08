package com.example.acmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.acmovies.fragments.CommentFragment;
import com.example.acmovies.fragments.ListEpisodeFragment;
import com.example.acmovies.fragments.MovieInformationFrangemt;
import com.example.acmovies.fragments.RelatedMoviesFragment;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.GlobalCheckAuth;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Status;
import com.example.acmovies.model.User;
import com.example.acmovies.model.Video;
import com.example.acmovies.model.WrapperData;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 1;
    private ImageView btnFullscreen;
    private MyViewPager mViewPager;
    private boolean fullscreen = true;
    private ProgressBar progressBar;
    private boolean isLoggedIn;
    private User user;
    private String token;

    public Movie movie;
    public Video video;
    public String video_url;
    public List<Episode> episodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = (Movie) getIntent().getSerializableExtra("movie");
//        video_url = movie.getVideoUrl();
        video = (Video) getIntent().getSerializableExtra("video");
        video_url = video.getVideoUrl();
        episodes = new ArrayList<>();

        inView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (movie.getEpisodes() == null)
        {
            mViewPager = new MyViewPager(fragmentManager, 3);
        }
        else{
//            getEpisodes();
            mViewPager = new MyViewPager(fragmentManager, 4);
        }
        viewPager.setAdapter(mViewPager);

        btnFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (fullscreen)
                {
                    btnFullscreen.setImageResource(R.drawable.exo_controls_fullscreen_exit);
                    getWindow().getDecorView().setSystemUiVisibility(v.SYSTEM_UI_FLAG_FULLSCREEN | v.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | v.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if (getSupportActionBar() != null)
                    {
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = false;

                }
                else
                {
                    btnFullscreen.setImageResource(R.drawable.exo_controls_fullscreen_enter);
                    getWindow().getDecorView().setSystemUiVisibility(v.SYSTEM_UI_FLAG_VISIBLE);
                    if (getSupportActionBar() != null)
                    {
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) (250 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen = true;
                }
            }
        });
    }

    private void inView()
    {
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        playerView = (PlayerView) findViewById(R.id.plvMovies);
        btnFullscreen = findViewById(R.id.exo_fullscreen);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);
        playerView.setFastForwardIncrementMs(10000);
        playerView.setRewindIncrementMs(10000);
    }

//    set PlayerView
    public void initializePlayer() {

        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(video_url);
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == player.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                }
                else if (playbackState == player.STATE_READY){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        player.prepare();
    }

    public void saveUserView(Episode episode){
        if (isLoggedIn){
            DataClient dataClient = APIUtils.getData();
            Call<Status> callSave;
            user = ((GlobalCheckAuth)getApplication()).getUser();
            token = ((GlobalCheckAuth)getApplication()).getToken();
            if (movie.getCategory().equals("phim bộ")){
                callSave = dataClient.SaveUerView("Bearer " + token, user.getId(), movie.getId(), episode.getId());
            } else {
                callSave = dataClient.SaveUerView("Bearer " + token, user.getId(), movie.getId(), 0);
            }
            callSave.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    Log.d("playvideo", response.body().getStatus()+"");
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    Log.d("playvideo", t.toString());
                }
            });

        } else {
            Log.d("playvideo", "isn't logged");
        }
    }

    public void getEpisodes(){
        DataClient dataClient = APIUtils.getData();
        Call<WrapperData<Episode>> callEpisodes = dataClient.getEpisodesbyMovie(movie.getId());
        callEpisodes.enqueue(new Callback<WrapperData<Episode>>() {
            @Override
            public void onResponse(Call<WrapperData<Episode>> call, Response<WrapperData<Episode>> response) {
                if (response.isSuccessful()){
                    for (Episode episode: response.body().getData()) {
                        episodes.add(episode);
                    }

                } else {
                    Log.d("Failed", "onResponse: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<WrapperData<Episode>> call, Throwable t) {
                Log.d("Error", "onFailure: "+t.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
        isLoggedIn = ((GlobalCheckAuth)getApplication()).isLogged();
        if (isLoggedIn){
            user = ((GlobalCheckAuth)getApplication()).getUser();
            token = ((GlobalCheckAuth)getApplication()).getToken();
//            if (movie.getCategory().equals("phim bộ")){
//                saveUserView(movie.getListepisode().get(0));
//            } else {
//                saveUserView(null);
//            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    public void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    public void exitPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    class MyViewPager extends FragmentStatePagerAdapter
    {
        private int slideCount ;

        public MyViewPager(@NonNull FragmentManager fm, int slideCount)
        {
            super(fm);
            this.slideCount = slideCount;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment frg = null;
            Bundle bundle = new Bundle();
            switch (position)
            {
                case 0:
                    frg = new MovieInformationFrangemt();

                    bundle.putString("title", movie.getName().substring(0, 1).toUpperCase() + movie.getName().substring(1));
                    bundle.putString("etitle", movie.getEngName().substring(0, 1).toUpperCase() + movie.getEngName().substring(1));
                    bundle.putString("subtitle", "Vietsub");
                    bundle.putString("quality", "full hd");
                    bundle.putString("descript", movie.getDescription());
                    bundle.putString("time", movie.getRuntime()+"");
                    bundle.putString("country", movie.getCountry().substring(0, 1).toUpperCase() + movie.getCountry().substring(1));
                    bundle.putString("category", movie.getCategory().substring(0, 1).toUpperCase() + movie.getCategory().substring(1));
                    bundle.putSerializable("directors", (Serializable) movie.getDirectors());
                    bundle.putSerializable("genres", (Serializable) movie.getGenres());
                    if (movie.getCategory().equals("phim bộ")){
                        bundle.putString("episode",  String.valueOf(movie.getEpisodes()));
                    }
                    else{
                        bundle.putString("episode", "1");
                    }

                    bundle.putString("years", movie.getReleasedate());
                    bundle.putSerializable("movie_id", movie.getId());
                    frg.setArguments(bundle);
                    break;
                case 1:
                    if (slideCount == 4) {
                        frg = new ListEpisodeFragment();
                        Log.d("episodes", "getItem: "+episodes.size());
                        bundle.putSerializable("movieId", movie.getId());
                        bundle.putSerializable("listep", (Serializable) episodes);
                        frg.setArguments(bundle);
                    }
                    else{
                        frg = new RelatedMoviesFragment();
                        bundle.putInt("movie_id", movie.getId());
                        frg.setArguments(bundle);
                    }
                    break;
                case 2:
                    if (slideCount == 4) {
                        frg = new RelatedMoviesFragment();
                        bundle.putInt("movie_id", movie.getId());
                        frg.setArguments(bundle);
                    } else {
                        frg = new CommentFragment();
                        bundle.putInt("movie_id", movie.getId());
                        frg.setArguments(bundle);
                    }
                    break;
                case 3:
                    if (slideCount == 4) {
                        frg = new CommentFragment();
                        bundle.putInt("movie_id", movie.getId());
                        frg.setArguments(bundle);
                    }
            }
            return frg;
        }

        @Override
        public int getCount() {
            return slideCount;
        }
    }
}
