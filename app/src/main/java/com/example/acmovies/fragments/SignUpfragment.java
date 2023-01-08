package com.example.acmovies.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.acmovies.MainActivity;
import com.example.acmovies.R;
import com.example.acmovies.model.Auth;
import com.example.acmovies.model.User;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpfragment extends Fragment {
    private View view;
    private ImageView btnBack;
    private TextInputLayout layoutName, layoutEmail, layoutPassword;
    private TextInputEditText txtName, txtEmail, txtPassword;
    private TextView txtSignIn;
    private Button btnSignUp;
    private boolean check = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        inView();
        return view;
    }

    private void inView() {
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(View -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new SignInFragment()).commit());

        layoutName = (TextInputLayout) view.findViewById(R.id.layoutNameSignUp);
        layoutEmail = (TextInputLayout) view.findViewById(R.id.layoutEmailSignUp);
        layoutPassword = (TextInputLayout) view.findViewById(R.id.layoutPasswordSignUp);
        txtName = (TextInputEditText) view.findViewById(R.id.txtNameSignUp);
        txtEmail = (TextInputEditText) view.findViewById(R.id.txtEmailSignUp);
        txtPassword = (TextInputEditText) view.findViewById(R.id.txtPasswordSignUp);
        txtSignIn = (TextView) view.findViewById(R.id.txtSignIn);
        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);

        txtSignIn.setOnClickListener(View -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new SignInFragment()).commit());

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    checkEmail();
                }
            }
        });

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtName.getText().toString().isEmpty()){
                    layoutName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtEmail.getText().toString().isEmpty()){
                    layoutEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(txtPassword.getText().toString().length() >7)){
                    layoutPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validate(){
        if (txtName.getText().toString().isEmpty()){
            layoutName.setErrorEnabled(true);
            layoutName.setError("Hãy nhập tên");
            return false;
        }
        if (txtEmail.getText().toString().isEmpty()){
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Hãy nhập địa chỉ Email");
            return false;
        }
        if (txtPassword.getText().toString().length()<8){
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Mật khẩu phải ít nhất 8 í tự");
            return false;
        }
        return true;
    }

    private void checkEmail(){
        DataClient dataClient = APIUtils.getData();
        Call<Boolean> callEmail = dataClient.CheckEmail(txtEmail.getText().toString().trim());
        callEmail.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()){
                    register();
                }
                else {
                    layoutEmail.setErrorEnabled(true);
                    layoutEmail.setError("Email đã tồn tại");
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
            }
        });
    }

    private  void register()
    {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString();

        DataClient dataClient = APIUtils.getData();
        Call<Auth> authCall = dataClient.Register(name, email, password);
        authCall.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (response.isSuccessful())
                {
                    Auth auth = response.body();
                    if (auth.getStatus()) {
//                        String token = auth.getToken();
//                        User user = auth.getUser();
//                        SharedPreferences userPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = userPref.edit();
//                        editor.putString("token", token);
//                        editor.putInt("id", user.getId());
//                        editor.putString("name", user.getName());
//                        editor.putString("email", user.getEmail());
//                        editor.putString("avatar", user.getAvatar());
//                        editor.putBoolean("isLoggedIn", true);
//                        editor.apply();
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra("flagFragment", "AccountFragment");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        getActivity().finish();
//                        progressDialog.dismiss();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Đăng ký không thành công", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage().toString());
            }
        });
    }
}
