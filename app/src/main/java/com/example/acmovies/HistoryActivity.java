package com.example.acmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.Interface.ItemDeleteClickListener;
import com.example.acmovies.adapter.HistoryRecyclerAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genre;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Status;
import com.example.acmovies.model.UserView;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity implements ItemClickListener, ItemDeleteClickListener {
    private RecyclerView recyclerView;
    private ImageView btnBack;
    private ProgressBar progressLoadData;
    private HistoryRecyclerAdapter historyRecyclerAdapter;
    private List<UserView> userViewList = new ArrayList<>();

    private int user_id;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        inView();
        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id", 0);
        token = intent.getStringExtra("token");
        getData();
    }

    private void inView(){
        btnBack = (ImageView) findViewById(R.id.btnBack);
        progressLoadData = (ProgressBar) findViewById(R.id.progres_loadData);
        btnBack.setOnClickListener(View -> onBackPressed());
        recyclerView = findViewById(R.id.recyclerHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        historyRecyclerAdapter = new HistoryRecyclerAdapter(this, userViewList, this, this);
        recyclerView.setAdapter(historyRecyclerAdapter);
    }

    private void getData(){
        progressLoadData.setVisibility(View.VISIBLE);
        DataClient dataClient = APIUtils.getData();
        Call<List<UserView>> call = dataClient.GetHistoryofUser("Bearer " + token, user_id);
        call.enqueue(new Callback<List<UserView>>() {
            @Override
            public void onResponse(Call<List<UserView>> call, Response<List<UserView>> response) {
                if (response.isSuccessful()){
                    progressLoadData.setVisibility(View.GONE);
                    List<UserView> list = response.body();
                    for (UserView userView : list){
                        userViewList.add(userView);
                    }
                    historyRecyclerAdapter.notifyDataSetChanged();
                } else {
                    progressLoadData.setVisibility(View.GONE);
                    Log.d("history", response.isSuccessful()+"loix");
                    getData();
                }
            }

            @Override
            public void onFailure(Call<List<UserView>> call, Throwable t) {
                progressLoadData.setVisibility(View.GONE);
                Log.d("history", t.toString());
                getData();
            }
        });
    }
    
//    Delete history
    private void deleteUserView(UserView userView){
        DataClient dataClient = APIUtils.getData();
        Call<Status> callDelete = dataClient.DeleteUerView("Bearer " + token, user_id, userView.getMovieId());
        callDelete.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus()){
                    userViewList.remove(userView);
                    historyRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HistoryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("history", t.toString());
            }
        });
    }

    @Override
    public void onMovieClick(Movie movie) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DataClient dataClient = APIUtils.getData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Call<Movie> callMovie = dataClient.GetMoviebyId(movie.getId());
//                callMovie.enqueue(new Callback<Movie>() {
//                    @Override
//                    public void onResponse(Call<Movie> call, Response<Movie> response) {
//                        Movie movie = response.body();
//                        Intent intent = new Intent(HistoryActivity.this, MovieDetailActivity.class);
//                        intent.putExtra("movie",movie);
//                        startActivity(intent);
//                        progressDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(Call<Movie> call, Throwable t) {
//                        progressDialog.dismiss();
//                        Log.d("ERROR: ", t.getMessage().toString());
//                    }
//                });
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
    public void onGenreClick(Genre genres) {

    }

    @Override
    public void deleteMovieClick(UserView userView) {
        deleteUserView(userView);
    }

    @Override
    public void deleteMovieClick(Movie movie) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}