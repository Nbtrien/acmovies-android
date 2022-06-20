package com.example.acmovies.retrofit;

import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Auth;
import com.example.acmovies.model.CommentPagination;
import com.example.acmovies.model.Director;
import com.example.acmovies.model.Genres;
import com.example.acmovies.model.ListMovie;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Pagination;
import com.example.acmovies.model.Status;
import com.example.acmovies.model.UserView;
import com.example.acmovies.model.Video;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DataClient {

    @GET("newmovies")
    Call<List<Movie>> GetNewMovie();

    @GET("hotmovies")
    Call<List<ListMovie>> GetHotMovie();

    @GET("hotgenres")
    Call<List<Genres>> GetHotGenre();

    @GET("allgenres")
    Call<List<Genres>> GetAllGenre();

    @GET("getallseries")
    Call<List<ListMovie>> GetAllSeries();

    @POST("listmovie/{category}")
    @FormUrlEncoded
    Call<Pagination> GetMoviesbyCategory(@Path("category") String category, @Field("page") int page);

    @POST("relatedmovies")
    @FormUrlEncoded
    Call<List<ListMovie>> GetRelatedMovies(@Field("id") Integer movie_id);

    @POST("moviesbygenre")
    @FormUrlEncoded
    Call<List<Movie>> GetMoviesbyGenre(@Field("id") Integer genre_id);

    @POST("commentsbymovie")
    @FormUrlEncoded
    Call<CommentPagination> GetCommentsbyMovie(@Field("id") Integer movie_id, @Field("page") int page);

    @POST("allmoviesbygenres")
    @FormUrlEncoded
    Call<Pagination> GetAllMoviesbyGenre(@Field("id") Integer genre_id, @Field("page") int page);

    @POST("movie")
    @FormUrlEncoded
    Call<Movie> GetMoviebyId(@Field("id") Integer movie_id);

    @POST("video")
    @FormUrlEncoded
    Call<Video> GetVideobyId(@Field("id") Integer video_id);

    @POST("actorsbymovie")
    @FormUrlEncoded
    Call<List<Actor>> GetActorsbyMovie(@Field("id") Integer movie_id);

    @POST("moviesbyactor")
    @FormUrlEncoded
    Call<List<Movie>> GetMoviesbyActor(@Field("id") Integer actor_id);

    @POST("directorsbymovie")
    @FormUrlEncoded
    Call<List<Director>> GetDirectorbyMovie(@Field("id") Integer movie_id);

    @POST("genresbymovie")
    @FormUrlEncoded
    Call<List<Genres>> GetGenresbyMovie(@Field("id") Integer movie_id);

    @POST("searchmovie")
    @FormUrlEncoded
    Call<Pagination> SearchMovie(@Field("key") String key, @Field("page") int page);

    @POST("login")
    @FormUrlEncoded
    Call<Auth> Login(@Field("email") String email, @Field("password") String password);

    @POST("checkemail")
    @FormUrlEncoded
    Call<Boolean> CheckEmail(@Field("email") String email);

    @POST("register")
    @FormUrlEncoded
    Call<Auth> Register(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @GET("checktoken")
    Call<Status> CheckToken(@Header("Authorization") String authHeader);

    @GET("logout")
    Call<Status> Logout(@Header("Authorization") String authHeader);

    @POST("storecomment")
    @FormUrlEncoded
    Call<Status> SaveComment(@Header("Authorization") String authHeader, @Field("movie_id") int movie_id, @Field("user_id") int user_id, @Field("content") String content);

    @POST("moviesbyuser")
    @FormUrlEncoded
    Call<List<Movie>> GetMoivesbyUser(@Header("Authorization") String authHeader, @Field("id") int user_id);

    @POST("historyofuser")
    @FormUrlEncoded
    Call<List<UserView>> GetHistoryofUser(@Header("Authorization") String authHeader, @Field("id") int user_id);

    @POST("checkusermovie")
    @FormUrlEncoded
    Call<Status> CheckMoviebyUser(@Header("Authorization") String authHeader, @Field("user_id") int user_id, @Field("movie_id") int movie_id);

    @POST("storeusermovie")
    @FormUrlEncoded
    Call<Status> SaveUerMovie(@Header("Authorization") String authHeader, @Field("user_id") int user_id, @Field("movie_id") int movie_id);

    @POST("deleteusermovie")
    @FormUrlEncoded
    Call<Status> DeleteUerMovie(@Header("Authorization") String authHeader, @Field("user_id") int user_id, @Field("movie_id") int movie_id);

    @POST("deleteuserview")
    @FormUrlEncoded
    Call<Status> DeleteUerView(@Header("Authorization") String authHeader, @Field("user_id") int user_id, @Field("movie_id") int movie_id);

    @POST("storeuserview")
    @FormUrlEncoded
    Call<Status> SaveUerView(@Header("Authorization") String authHeader, @Field("user_id") int user_id, @Field("movie_id") int movie_id, @Field("episode_id") int episode_id);
}
