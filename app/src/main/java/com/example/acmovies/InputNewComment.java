package com.example.acmovies;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.acmovies.Interface.DialogCloseListener;
import com.example.acmovies.model.Status;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.ContextCompat.getSystemService;

public class InputNewComment extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private TextInputEditText txtComment;
    private Button btnSave;
    private ImageView btnClose;
    private boolean status = false;
    private int movie_id, user_id;
    private String token;
    private DialogCloseListener closeListener;

    public InputNewComment(int movie_id, int user_id, String token, DialogCloseListener closeListener) {
        this.movie_id = movie_id;
        this.user_id = user_id;
        this.token = token;
        this.closeListener = closeListener;
    }

    public static InputNewComment newInstance(int movie_id, int user_id, String token, DialogCloseListener closeListener)
    {
        return new InputNewComment(movie_id, user_id, token, closeListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(getView(), 0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_new_comment, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtComment = getView().findViewById(R.id.inputNewComment);
        btnSave = getView().findViewById(R.id.btnSaveComment);
        btnClose = getView().findViewById(R.id.imgClose);

        btnSave.setEnabled(false);
        txtComment.requestFocus();

        txtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().trim().isEmpty()){
                    btnSave.setEnabled(true);
                } else btnSave.setEnabled(false);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                saveComment();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void saveComment(){
        DataClient dataClient = APIUtils.getData();
        Map<String, String> params = new HashMap<>();
        params.put("movie_id", String.valueOf(movie_id));
        params.put("user_id", String.valueOf(user_id));
        String content = txtComment.getText().toString();
        params.put("content", content);

        Call<Status> callStoreComment = dataClient.postComment("Bearer " + token,params);
        callStoreComment.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.body().getStatus()){
                    status = true;
                    dismiss();
                } else {
                    status = false;
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("ERROR:", t.toString());
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Log.d("closedialog:", "Successful");
        closeListener.callbackMethod(status);
    }
}
