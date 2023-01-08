package com.example.acmovies.retrofit;

public class APIUtils {
//    public static final String Base_Url = "https://tr-movies-api.herokuapp.com/api/";
public static final String Base_Url = "http://192.168.1.6:8000/api/";
    public static DataClient getData()
    {
        return RetrofitClient.getClient(Base_Url).create(DataClient.class);

    }
}
