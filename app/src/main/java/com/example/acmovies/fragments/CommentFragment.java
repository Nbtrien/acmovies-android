package com.example.acmovies.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.example.acmovies.AuthActivity;
import com.example.acmovies.InputNewComment;
import com.example.acmovies.Interface.DialogCloseListener;
import com.example.acmovies.MainActivity;
import com.example.acmovies.R;
import com.example.acmovies.adapter.CommentRecyclerAdapter;
import com.example.acmovies.model.Comment;
import com.example.acmovies.model.CommentPagination;
import com.example.acmovies.model.GlobalCheckAuth;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.User;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment implements DialogCloseListener {
    private View view;
    private RecyclerView recyclerComments;
    private TextView txtCommentnumber;
    private CommentPagination commentPagination;
    private TextInputEditText txtComment;
    private List<Comment> commentList;
    private CommentRecyclerAdapter commentAdapter;
    private int movie_id;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private boolean inLoggedIn;
    private User user;
    private Dialog dialog;
    private DialogCloseListener closeListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment, container, false);
        closeListener = this;

        inView();
        Bundle bundle = getArguments();
        movie_id = bundle.getInt("movie_id");
        getData();
        return view;
    }

    private void inView() {
        dialog = new Dialog(getContext());
        inLoggedIn = ((GlobalCheckAuth)getActivity().getApplication()).isLogged();

        txtCommentnumber = (TextView) view.findViewById(R.id.txtCommentNumber);
        recyclerComments = (RecyclerView) view.findViewById(R.id.recyclerComment);
        txtComment = (TextInputEditText) view.findViewById(R.id.newComment);
        if (inLoggedIn){
            user = ((GlobalCheckAuth)getActivity().getApplication()).getUser();
            String token = ((GlobalCheckAuth)getActivity().getApplication()).getToken();
            txtComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputNewComment.newInstance(movie_id, user.getId(), token, closeListener).show(getActivity().getSupportFragmentManager(),InputNewComment.TAG);
                }
            });
        } else {
            txtComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLoginDialog();
                }
            });
        }
        recyclerComments.setHasFixedSize(true);
        recyclerComments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        commentList = new ArrayList<>();
        commentAdapter = new CommentRecyclerAdapter(getContext(), commentList);
        recyclerComments.setAdapter(commentAdapter);
        recyclerComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(recyclerComments.canScrollVertically(1)){
                    if (currentPage <= totalAvailablePages){
                        currentPage += 1;
                        getData();
                    }
                }
            }
        });
    }

    private void getData() {
        DataClient dataClient = APIUtils.getData();
        Call<CommentPagination> callComments = dataClient.GetCommentsbyMovie(movie_id, currentPage);
        callComments.enqueue(new Callback<CommentPagination>() {
            @Override
            public void onResponse(Call<CommentPagination> call, Response<CommentPagination> response) {
                if (response.isSuccessful()){
                    CommentPagination pagination = response.body();
                    totalAvailablePages = pagination.getLastPage();
                    Integer commentNumber = pagination.getTotal();
                    txtCommentnumber.setText("Bình luận ("+commentNumber+")");
                    for (Comment comment : pagination.getComments()) {
                        commentList.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();
                } else {
                    getData();
                }
            }

            @Override
            public void onFailure(Call<CommentPagination> call, Throwable t) {
                Log.d("ERROR:", t.toString());
                getData();
            }
        });
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
    public void callbackMethod(boolean status) {
        if (status == true){
            currentPage = 1;
            totalAvailablePages = 1;
            commentList.clear();
            getData();
        }
    }
}
