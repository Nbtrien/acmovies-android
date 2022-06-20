package com.example.acmovies.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acmovies.ActorBottomSheet;
import com.example.acmovies.AuthActivity;
import com.example.acmovies.Interface.ItemClickListener;
import com.example.acmovies.R;
import com.example.acmovies.adapter.ActorRecyclerAdapter;
import com.example.acmovies.adapter.MoviePropertiesAdapter;
import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Director;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.GlobalCheckAuth;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.MovieProperties;
import com.example.acmovies.model.Status;
import com.example.acmovies.model.User;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieInformationFrangemt extends Fragment implements ItemClickListener {
    private View view;
    private TextView txt_name, txt_ename, txt_subtitle, txt_quality, txt_content;
    private RecyclerView recycler_properties, recycler_actor;
    private Button btnAddLibrary, btnShare, btnDownlod;

    private List<MovieProperties> moviePropertiesList;
    private List<Actor> actorList;
    private String genresList = "", directorList = "";
    private MoviePropertiesAdapter moviePropertiesAdapter;
    private ActorRecyclerAdapter actorRecyclerAdapter;
    private int movie_id;
    private Bundle bundle;
    private DataClient dataClient = APIUtils.getData();
    private boolean isLoggedIn;
    private User user;
    private String token;
    private Dialog dialog;
    private boolean isStored = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie_information, container, false);

        bundle = getArguments();
        movie_id = (int) bundle.getSerializable("movie_id");

        inView();

        moviePropertiesList = new ArrayList<>();
        recycler_properties.setHasFixedSize(true);
        LinearLayoutManager layoutVertical = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recycler_properties.setLayoutManager(layoutVertical);
        moviePropertiesAdapter = new MoviePropertiesAdapter(getContext(), moviePropertiesList);
        recycler_properties.setAdapter(moviePropertiesAdapter);

        actorList = new ArrayList<>();
        recycler_actor.setHasFixedSize(true);
        LinearLayoutManager layoutHorizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_actor.setLayoutManager(layoutHorizontal);
        actorRecyclerAdapter = new ActorRecyclerAdapter(getContext(), actorList, this);
        recycler_actor.setAdapter(actorRecyclerAdapter);

        String category = bundle.getString("category");

        setData();
        getDirectors();
        getGenres();
        getActors();
        return view;
    }

    private void inView()
    {
        isLoggedIn = ((GlobalCheckAuth)getActivity().getApplication()).isLogged();
        dialog = new Dialog(getContext());
        txt_name = (TextView) view.findViewById(R.id.txt_Moviename);
        txt_ename = (TextView)  view.findViewById(R.id.txt_Movieename);
        txt_subtitle = (TextView)  view.findViewById(R.id.txt_subtitle);
        txt_quality = (TextView)  view.findViewById(R.id.txt_quality);
        txt_content = (TextView)  view.findViewById(R.id.txt_content);
        recycler_properties = (RecyclerView)  view.findViewById(R.id.recycler_MovieInfor);
        recycler_actor = (RecyclerView) view.findViewById(R.id.recycler_actor);
        btnAddLibrary = (Button) view.findViewById(R.id.btn_addlibra);
        btnShare = (Button) view.findViewById(R.id.btn_share);
        btnDownlod = (Button) view.findViewById(R.id.btn_download);

        if (isLoggedIn){
            user = ((GlobalCheckAuth)getActivity().getApplication()).getUser();
            token = ((GlobalCheckAuth)getActivity().getApplication()).getToken();
            checkMoviebyUser();
            btnAddLibrary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isStored){
                        deleteUserMovie();
                    } else {
                        saveUserMovie();
                    }

                }
            });
        } else {
            btnAddLibrary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLoginDialog();
                }
            });
        }

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlSend = "http://192.168.1.8:8000/movie/"+bundle.getString("title")+"/"+movie_id;
                Intent intentSend = new Intent(Intent.ACTION_SEND);
                intentSend.putExtra(Intent.EXTRA_TEXT, urlSend);
                intentSend.setType("text/plain");
                getContext().startActivity(Intent.createChooser(intentSend, bundle.getString("title")));
            }
        });


    }

//    check movie by user
    private void checkMoviebyUser(){
        Call<Status> callCheck = dataClient.CheckMoviebyUser("Bearer " + token, user.getId(), movie_id);
        callCheck.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Log.d("checkMoviebyUser", response.body().getStatus() + "=>" + response.body().getMessage());
                        Drawable img = getContext().getResources().getDrawable(R.drawable.ic_check);
                        btnAddLibrary.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);
                        isStored = true;
                    } else {
                        Log.d("checkMoviebyUser", response.body().getStatus() + "=>" + response.body().getMessage());
                        isStored = false;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("ERROR:", t.toString());
                checkMoviebyUser();
            }
        });
    }

//    check movie by user
    private void saveUserMovie(){
        Call<Status> callCheck = dataClient.SaveUerMovie("Bearer " + token, user.getId(), movie_id);
        callCheck.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus()){
                    Log.d("checkMoviebyUser", response.body().getStatus()+"=>"+response.body().getMessage());
                    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_check );
                    btnAddLibrary.setCompoundDrawablesWithIntrinsicBounds( null, img, null, null );
                    isStored = true;
                    Toast.makeText(getContext(), "Đã lưu vào danh sách phim", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("ERROR:", t.toString());
            }
        });
    }

//    Delete movie out of list
    private void deleteUserMovie(){
        Call<Status> callCheck = dataClient.DeleteUerMovie("Bearer " + token, user.getId(), movie_id);
        callCheck.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus()){
                    Log.d("checkMoviebyUser", response.body().getStatus()+"=>"+response.body().getMessage());
                    Drawable img = getContext().getResources().getDrawable( R.drawable.ic_plus );
                    btnAddLibrary.setCompoundDrawablesWithIntrinsicBounds( null, img, null, null );
                    isStored = false;
                    Toast.makeText(getContext(), "Đã xóa khỏi danh sách phim", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("ERROR:", t.toString());
            }
        });
    }

//    get actors
    private void getActors()
    {
        Call<List<Actor>> listActorCall = dataClient.GetActorsbyMovie(movie_id);
        listActorCall.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                if (response.isSuccessful()){
                    ArrayList<Actor> list = (ArrayList<Actor>) response.body();
                    for (Actor actor : list){
                        actorList.add(actor);
                    }
                    actorRecyclerAdapter.notifyDataSetChanged();
                } else {
                    getActors();
                }
            }

            @Override
            public void onFailure(Call<List<Actor>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
                getActors();
            }
        });
    }
//  get directors
    private void getDirectors()
    {
        Call<List<Director>> listDirectorCall = dataClient.GetDirectorbyMovie(movie_id);
        listDirectorCall.enqueue(new Callback<List<Director>>() {
            @Override
            public void onResponse(Call<List<Director>> call, Response<List<Director>> response) {
                ArrayList<Director> list = (ArrayList<Director>) response.body();
                for (Director director : list){
                    if (director == list.get(list.size()-1))
                        directorList += director.getName();
                    else
                        directorList += director.getName()+", ";
                }
                moviePropertiesList.add(new MovieProperties("Thời lượng",bundle.getString("time")+" phút"));
                moviePropertiesList.add(new MovieProperties("Đạo diễn", directorList));

                moviePropertiesList.add(new MovieProperties("Quốc gia", bundle.getString("country")));
                moviePropertiesList.add(new MovieProperties("Số tập", bundle.getString("episode")));
                moviePropertiesList.add(new MovieProperties("Phát hành", bundle.getString("years")));

                moviePropertiesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Director>> call, Throwable t) {
                getDirectors();
                Log.d("ERROR: ", t.getMessage().toString());
            }
        });
    }

//  Get Genres
    private void getGenres()
    {
        Call<List<Genres>> listGenresCall = dataClient.GetGenresbyMovie(movie_id);
        listGenresCall.enqueue(new Callback<List<Genres>>() {
            @Override
            public void onResponse(Call<List<Genres>> call, Response<List<Genres>> response) {
                if (response.isSuccessful()){
                    ArrayList<Genres> list = (ArrayList<Genres>) response.body();
                    for (Genres genres : list){
                        if (genres == list.get(list.size()-1))
                            genresList += genres.getName();
                        else
                            genresList += genres.getName()+", ";
                    }
                    moviePropertiesList.add(new MovieProperties("Thể loại", genresList));
                    moviePropertiesAdapter.notifyDataSetChanged();
                } else {
                    getGenres();
                }

            }

            @Override
            public void onFailure(Call<List<Genres>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
                getGenres();
            }
        });
    }

    private void setData()
    {
        txt_name.setText(bundle.getString("title"));
        txt_ename.setText(bundle.getString("etitle"));
        txt_subtitle.setText(bundle.getString("subtitle"));
        txt_quality.setText(bundle.getString("quality"));
        txt_content.setText(Html.fromHtml(bundle.getString("descript")));
    }

    private void openLoginDialog(){
        dialog.setContentView(R.layout.signin_layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnLogout = (Button) dialog.findViewById(R.id.btnLogin);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                login();
            }
        });
        dialog.show();
    }

    private void login(){
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onMovieClick(Movie movie) {

    }

    @Override
    public void onEpisodeClick(Episode episode) {

    }

    @Override
    public void onActorClick(Actor actor) {
        ActorBottomSheet.newInstance(actor).show(getActivity().getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void onGenreClick(Genres genres) {

    }
}
