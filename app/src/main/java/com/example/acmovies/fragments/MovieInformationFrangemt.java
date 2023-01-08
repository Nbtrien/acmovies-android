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
import com.example.acmovies.model.Genre;
import com.example.acmovies.model.GlobalCheckAuth;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.MovieProperties;
import com.example.acmovies.model.Status;
import com.example.acmovies.model.User;
import com.example.acmovies.model.WrapperData;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        getActors();
        setData();

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
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user.getId()));
        params.put("movie_id", String.valueOf(movie_id));

        Call<Status> callCheck = dataClient.CheckMoviebyUser("Bearer " + token, params);
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
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user.getId()));
        params.put("movie_id", String.valueOf(movie_id));

        Call<Status> callCheck = dataClient.saveUerMovie("Bearer " + token, params);
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
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(user.getId()));
        params.put("movie_id", String.valueOf(movie_id));

        Call<Status> callCheck = dataClient.deleteUerMovie("Bearer " + token, params);
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
        Call<WrapperData<Actor>> listActorCall = dataClient.GetActorsbyMovie(movie_id);
        listActorCall.enqueue(new Callback<WrapperData<Actor>>() {
            @Override
            public void onResponse(Call<WrapperData<Actor>> call, Response<WrapperData<Actor>> response) {
                if (response.isSuccessful()){
                    ArrayList<Actor> list = (ArrayList<Actor>) response.body().getData();
                    for (Actor actor : list){
                        actorList.add(actor);
                    }
                    actorRecyclerAdapter.notifyDataSetChanged();

                } else {
                    getActors();
                }
            }

            @Override
            public void onFailure(Call<WrapperData<Actor>> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
                getActors();
            }
        });
    }

    private void setData()
    {
        Log.d("setdata", "setData: ");
        txt_name.setText(bundle.getString("title"));
        txt_ename.setText(bundle.getString("etitle"));
        txt_subtitle.setText(bundle.getString("subtitle"));
        txt_quality.setText(bundle.getString("quality"));
        txt_content.setText(Html.fromHtml(bundle.getString("descript")));

        ArrayList<Director> directors = (ArrayList<Director>) bundle.getSerializable("directors");
        for (Director director : directors){
            if (director == directors.get(directors.size()-1))
                directorList += (director.getName().substring(0, 1).toUpperCase() + director.getName().substring(1));
            else
                directorList += (director.getName().substring(0, 1).toUpperCase() + director.getName().substring(1)+", ");
        }


        ArrayList<Genre> genres = (ArrayList<Genre>) bundle.getSerializable("genres");
        for (Genre genre : genres){
            if (genre == genres.get(genres.size()-1))
                genresList += (genre.getName().substring(0, 1).toUpperCase() + genre.getName().substring(1));
            else
                genresList += (genre.getName().substring(0, 1).toUpperCase() + genre.getName().substring(1)+", ");
        }
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = null;
        try {
            convertedDate = parser.parse(bundle.getString("years"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String realeaseDate = formatter.format(convertedDate);


        moviePropertiesList.add(new MovieProperties("Thể loại", genresList));
        moviePropertiesList.add(new MovieProperties("Thời lượng",bundle.getString("time")+" phút"));
        moviePropertiesList.add(new MovieProperties("Đạo diễn", directorList));

        moviePropertiesList.add(new MovieProperties("Quốc gia", bundle.getString("country")));
        moviePropertiesList.add(new MovieProperties("Số tập", bundle.getString("episode")));

        moviePropertiesList.add(new MovieProperties("Phát hành", realeaseDate));
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
    public void onGenreClick(Genre genres) {

    }
}
