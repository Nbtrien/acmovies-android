package com.example.acmovies.retrofit;

public class APIUtils {
    public static final String Base_Url = "http://192.168.1.14:8000/api/";
    public static DataClient getData()
    {
        return RetrofitClient.getClient(Base_Url).create(DataClient.class);
    }
}
