package com.example.acmovies.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.acmovies.AuthActivity;
import com.example.acmovies.HistoryActivity;
import com.example.acmovies.MainActivity;
import com.example.acmovies.MyMovieActivity;
import com.example.acmovies.R;
import com.example.acmovies.model.GlobalCheckAuth;
import com.example.acmovies.model.Status;
import com.example.acmovies.model.User;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private View view;
    private boolean isloggedIn;
    private User user;
    private String token;
    private LinearLayout layoutNotLogged, layoutLogged, itemAccount, itemLogout, itemWatchList, itemHistory;
    private TextView txtUserName, txtEmail;
    private Dialog dialog;
    private CircleImageView imgAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isloggedIn = ((GlobalCheckAuth)getActivity().getApplication()).isLogged();
        view = inflater.inflate(R.layout.fragment_account,container,false);

        inView();

        return view;
    }

    private void inView(){
        dialog = new Dialog(getContext());
        layoutLogged = (LinearLayout) view.findViewById(R.id.layoutLogged);
        layoutNotLogged = (LinearLayout) view.findViewById(R.id.layoutNotLogged);
        itemAccount = (LinearLayout) view.findViewById(R.id.itemAccount);
        itemLogout = (LinearLayout) view.findViewById(R.id.itemLogout);
        itemWatchList = (LinearLayout) view.findViewById(R.id.itemWatchList);
        itemHistory = (LinearLayout) view.findViewById(R.id.itemHistory);
        txtUserName = (TextView) view.findViewById(R.id.txtUserName);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        imgAvatar = (CircleImageView) view.findViewById(R.id.imgAvatar);

        if (!isloggedIn){
            layoutNotLogged.setVisibility(View.VISIBLE);
            layoutLogged.setVisibility(View.GONE);
            txtEmail.setVisibility(View.GONE);
            itemAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AuthActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }
        else {
            user = ((GlobalCheckAuth)getActivity().getApplication()).getUser();
            token = ((GlobalCheckAuth)getActivity().getApplication()).getToken();
            layoutNotLogged.setVisibility(View.GONE);
            layoutLogged.setVisibility(View.VISIBLE);
            txtEmail.setVisibility(View.VISIBLE);
            String name = user.getName().substring(0, 1).toUpperCase() + user.getName().substring(1).toLowerCase();
            txtUserName.setText(name);
            txtEmail.setText(user.getEmail());
        }

        itemLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogoutDialog();
            }
        });

        itemWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyMovieActivity.class);
                intent.putExtra("user_id", user.getId());
                intent.putExtra("token", token);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        itemHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                intent.putExtra("user_id", user.getId());
                intent.putExtra("token", token);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void openLogoutDialog(){
        dialog.setContentView(R.layout.logout_layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnLogout = (Button) dialog.findViewById(R.id.btnLogout);

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
                logout();
            }
        });
        dialog.show();
    }

    private void logout(){
        Log.d("Logout", token);
        DataClient dataClient = APIUtils.getData();
        Call<Status> callLogout = dataClient.Logout("Bearer " + token);
        callLogout.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus()){
                    SharedPreferences userPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                    userPref.edit().clear().commit();
                    GlobalCheckAuth checkAuth = (GlobalCheckAuth)getActivity().getApplication();
                    checkAuth.setLogged(false);
                    checkAuth.setUser(new User());
                    checkAuth.setToken("");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    getActivity().overridePendingTransition(0, 0);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Đăng xuất không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("Logout", t.toString());
            }
        });
    }

}
