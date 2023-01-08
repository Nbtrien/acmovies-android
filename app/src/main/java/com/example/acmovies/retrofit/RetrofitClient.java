package com.example.acmovies.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String base_url)
    {
        OkHttpClient builder = new OkHttpClient.Builder()
                                        .readTimeout(30, TimeUnit.SECONDS)
                                        .writeTimeout(30, TimeUnit.SECONDS)
                                        .connectTimeout(30,TimeUnit.SECONDS)
                                        .retryOnConnectionFailure(true)
                                        .build();
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .client(builder)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        return retrofit;
    }
}
