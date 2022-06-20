package com.example.acmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.acmovies.fragments.DiscoverFragment;
import com.example.acmovies.fragments.HomeFragment;
import com.example.acmovies.fragments.DownloadFragment;
import com.example.acmovies.fragments.AccountFragment;
import com.example.acmovies.model.GlobalCheckAuth;
import com.example.acmovies.model.Status;
import com.example.acmovies.model.User;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mbottomNav;
    private FrameLayout mframe;
    private ImageView imgSearch;
    private LinearLayout headerLayout;
    private GlobalCheckAuth checkAuth;
    private String token;
    private SharedPreferences userPref;
    private boolean isloggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inView();
        mbottomNav.setOnNavigationItemSelectedListener(selectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, new HomeFragment()).commit();
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void inView() {
        mbottomNav = (BottomNavigationView) findViewById(R.id.bott_nav);
        mframe = (FrameLayout) findViewById(R.id.fragment_main);
        imgSearch = (ImageView) findViewById(R.id.img_Search);
        headerLayout = (LinearLayout) findViewById(R.id.layoutHeader);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.nav_home:
                    setFragment(new HomeFragment());
                    return true;

                case R.id.nav_discover:
                    setFragment(new DiscoverFragment());
                    return true;

                case R.id.nav_download:
                    setFragment(new DownloadFragment());
                    return true;

                case  R.id.nav_account:
                    setFragment(new AccountFragment());
                    return true;

                default: return false;
            }
        }
    };

    private void setFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, fragment).commit();
    }

    private void checkToken(){
        DataClient dataClient = APIUtils.getData();
        Call<Status> callCheckToken = dataClient.CheckToken("Bearer " + token);
        callCheckToken.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus()){
                    checkAuth.setLogged(isloggedIn);
                    User user = new User();
                    user.setId(userPref.getInt("id", 0));
                    user.setName(userPref.getString("name", ""));
                    user.setEmail(userPref.getString("email", ""));
                    user.setAvatar(userPref.getString("avatar", ""));
                    checkAuth.setUser(user);
                    checkAuth.setToken(userPref.getString("token", ""));
                    Log.d("checkToken", response.body().getStatus()+" "+userPref.getString("name",""));

                } else {
                    userPref.edit().clear().commit();
                    Log.d("checkToken", response.body().getStatus()+" "+userPref.getString("name",""));
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("checkToken", t.toString());
                checkToken();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userPref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        isloggedIn = userPref.getBoolean("isLoggedIn", false);
        checkAuth = (GlobalCheckAuth)getApplication();
        Intent intent =  getIntent();
        if (isloggedIn) {
            token = userPref.getString("token", "");
            checkToken();
        } else if (intent.hasExtra("flagFragment")) {
            String fragName = intent.getStringExtra("flagFragment");
            if (fragName.equals("AccountFragment")){
                mbottomNav.setSelectedItemId(R.id.nav_account);
            }
        }
    }

}