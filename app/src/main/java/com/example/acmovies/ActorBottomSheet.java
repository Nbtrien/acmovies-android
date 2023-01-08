package com.example.acmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.adapter.ItemRecyclerAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genre;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Video;
import com.example.acmovies.model.WrapperData;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorBottomSheet extends BottomSheetDialogFragment implements ItemClickListener {
    private Actor actor;
    private CircleImageView imageView;
    private TextView txtName, txtInfor;
    private ImageButton btnClose;
    private RecyclerView recyclerView;

    private ItemRecyclerAdapter itemRecyclerAdapter;
    private List<Movie> movieList;

    public ActorBottomSheet(Actor actor) {
        this.actor = actor;
    }

    public static ActorBottomSheet newInstance(Actor actor)
    {
        return new ActorBottomSheet(actor);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actor_layout_bottom_sheet, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inView();
        getMovies();
    }

    private void inView(){
        imageView = (CircleImageView) getView().findViewById(R.id.imgActor);
        txtName = (TextView) getView().findViewById(R.id.txtActorName);
        txtInfor = (TextView) getView().findViewById(R.id.txtActorInfor);
        btnClose = (ImageButton) getView().findViewById(R.id.btnClose);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerActorMovies);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        movieList = new ArrayList<>();
        itemRecyclerAdapter = new ItemRecyclerAdapter(getContext(), movieList, this);
        recyclerView.setAdapter(itemRecyclerAdapter);

        Glide.with(getContext()).load(actor.getImage().getImageUrl()).into(imageView);
        txtName.setText(actor.getName().toUpperCase());
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = null;
        try {
            convertedDate = parser.parse(actor.getBirthday());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String birthday = formatter.format(convertedDate);
        txtInfor.setText(birthday);
//        txtInfor.setText(actor.getBirthday()+" - "+actor.getCountry().getName());
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void getMovies() {
        DataClient dataClient = APIUtils.getData();
        Map<String, String> params = new HashMap<>();
        params.put("actor_id", String.valueOf(actor.getId()));
        Call<WrapperData<Movie>> callMovies = dataClient.getMoviesbyActor(params);
        callMovies.enqueue(new Callback<WrapperData<Movie>>() {
            @Override
            public void onResponse(Call<WrapperData<Movie>> call, Response<WrapperData<Movie>> response) {
                if (response.isSuccessful()){
                    ArrayList<Movie> list = (ArrayList<Movie>) response.body().getData();
                    for (Movie movie : list){
                        movieList.add(movie);
                    }
                    itemRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ERRORMOVIEACTOR", response.isSuccessful()+"");
                    getMovies();
                }
            }

            @Override
            public void onFailure(Call<WrapperData<Movie>> call, Throwable t) {
                Log.d("ERRORMOVIEACTOR", t.toString());
                getMovies();
            }
        });
    }

    @Override
    public void onMovieClick(Movie movie) {
        sendtoMovieDetail(movie.getId());
//        DataClient dataClient = APIUtils.getData();
//        Call<Movie> callMovie = dataClient.GetMoviebyId(movie.getId());
//        callMovie.enqueue(new Callback<Movie>() {
//            @Override
//            public void onResponse(Call<Movie> call, Response<Movie> response) {
//                Movie movie = response.body();
//                Intent intent = new Intent(getContext(),MovieDetailActivity.class);
//                intent.putExtra("movie",movie);
//                getContext().startActivity(intent);
//                dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<Movie> call, Throwable t) {
//                Log.d("ERROR: ", t.getMessage().toString());
//            }
//        });
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
}
