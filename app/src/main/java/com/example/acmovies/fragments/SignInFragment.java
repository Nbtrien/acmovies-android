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
import com.example.acmovies.model.AuthWrapper;
import com.example.acmovies.model.User;
import com.example.acmovies.retrofit.APIUtils;
import com.example.acmovies.retrofit.DataClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends Fragment {
    private View view;
    private ImageView btnBack;
    private TextInputLayout layoutEmail, layoutPassword;
    private TextInputEditText txtEmail, txtPassword;
    private TextView txtSignUp;
    private Button btnSignIn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        inView();
        return view;
    }

    private void inView() {
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(View -> getActivity().onBackPressed());

        layoutEmail = (TextInputLayout) view.findViewById(R.id.layoutEmailSignIn);
        layoutPassword = (TextInputLayout) view.findViewById(R.id.layoutPasswordSignIn);
        txtEmail = (TextInputEditText) view.findViewById(R.id.txtEmailSignIn);
        txtPassword = (TextInputEditText) view.findViewById(R.id.txtPasswordSignIn);
        txtEmail.setText("trien@gmail.com");
        txtPassword.setText("123456789");
        txtSignUp = (TextView) view.findViewById(R.id.txtSignUp);
        btnSignIn = (Button) view.findViewById(R.id.btnSignIn);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new SignUpfragment()).commit();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    login();
                }
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
    private  void login()
    {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString();

        DataClient dataClient = APIUtils.getData();
        Call<AuthWrapper<Auth>> authCall = dataClient.Login(email, password);
        authCall.enqueue(new Callback<AuthWrapper<Auth>>() {
            @Override
            public void onResponse(Call<AuthWrapper<Auth>> call, Response<AuthWrapper<Auth>> response) {
                if (response.isSuccessful())
                {
                    Auth auth = response.body().getData();
                    if (auth.getStatus()) {
                        String token = auth.getAccessToken();
                        User user = auth.getUser();
                        SharedPreferences userPref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("token", token);
                        editor.putInt("id", user.getId());
                        editor.putString("name", user.getName());
                        editor.putString("email", user.getEmail());
                        editor.putString("avatar", user.getAvatar());
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                        progressDialog.dismiss();
                    }
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthWrapper<Auth>> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
