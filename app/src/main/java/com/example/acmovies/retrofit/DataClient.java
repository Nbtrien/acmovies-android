package com.example.acmovies.retrofit;

import com.example.acmovies.model.Actor;
import com.example.acmovies.model.Auth;
import com.example.acmovies.model.AuthWrapper;
import com.example.acmovies.model.Comment;
import com.example.acmovies.model.CommentPagination;
import com.example.acmovies.model.Episode;
import com.example.acmovies.model.Movie;
import com.example.acmovies.model.Series;
import com.example.acmovies.model.WrapperData;
import com.example.acmovies.model.Director;
import com.example.acmovies.model.Genre;
import com.example.acmovies.model.ListMovie;
import com.example.acmovies.model.Pagination;
import com.example.acmovies.model.Status;
import com.example.acmovies.model.UserView;
import com.example.acmovies.model.Video;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface DataClient {

    @GET("movies")
    Call<WrapperData<Movie>> getMovies(@QueryMap Map<String, String> params);

    @GET("series/{series_id}/movies")
    Call<WrapperData<Movie>> getMoviesbySeries(@Path("series_id") int series_id);

    //  new get movie
    @GET("movie/{id}")
    Call<Movie> getMovie(@Path("id") Integer id);

    //  new get video
    @GET("movie/{id}/video")
    Call<Video> getVideobyMovie(@Path("id") Integer id);

    @GET("video/{video_id}")
    Call<Video> getVideo(@Path("video_id") Integer id);

    @GET("genres")
    Call<WrapperData<Genre>> getGenres(@Query("limit") int limit);

    @GET("movie/{movie_id}/actors")
    Call<WrapperData<Actor>> GetActorsbyMovie(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/episodes")
    Call<WrapperData<Episode>> getEpisodesbyMovie(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/similar")
    Call<WrapperData<Movie>> getSimilaMovie(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/sameseries")
    Call<WrapperData<ListMovie>> getSameSiries(@Path("movie_id") int movie_id);

    @GET("genres")
    Call<WrapperData<Genre>> getGenres(@QueryMap Map<String, String> params);

    @GET("series")
    Call<WrapperData<Series>> getSeries(@QueryMap Map<String, String> params);

    //Search
    @GET("search/movie/{key}")
    Call<WrapperData<Movie>> getMoviebyKey(@QueryMap Map<String, String> params);

    //Get comments
    @GET("movie/{movie_id}/comments")
    Call<WrapperData<Comment>> getComments(@QueryMap Map<String, String> params);

    //Get movies by actor
    @GET("actor/{actor_id}/movies")
    Call<WrapperData<Movie>> getMoviesbyActor(@QueryMap Map<String, String> params);

    //Login
    @POST("login")
    @FormUrlEncoded
    Call<AuthWrapper<Auth>> Login(@Field("email") String email, @Field("password") String password);

    @POST("checkemail")
    @FormUrlEncoded
    Call<Boolean> CheckEmail(@Field("email") String email);

    @POST("register")
    @FormUrlEncoded
    Call<Auth> Register(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @POST("check_token")
    Call<Status> CheckToken(@Header("Authorization") String authHeader);

    @POST("logout")
    Call<Status> Logout(@Header("Authorization") String authHeader);

    @GET("user/{user_id}/movies")
    Call<WrapperData<Movie>> getMoivesbyUser(@Header("Authorization") String authHeader, @QueryMap Map<String, String> params);

    @GET("user/{user_id}/movie/{movie_id}")
    Call<Status> CheckMoviebyUser(@Header("Authorization") String authHeader, @QueryMap Map<String, String> params);

    @POST("usermovies")
    Call<Status> saveUerMovie(@Header("Authorization") String authHeader, @QueryMap Map<String, String> params);

    @PUT("usermovies")
    Call<Status> deleteUerMovie(@Header("Authorization") String authHeader, @QueryMap Map<String, String> params);

    // post comment
    @POST("comments")
    Call<Status> postComment(@Header("Authorization") String authHeader, @QueryMap Map<String, String> params);











//    @GET("allgenres")
//    Call<List<Genre>> GetAllGenre();
//
//    @GET("getallseries")
//    Call<List<ListMovie>> GetAllSeries();
//
//    @POST("listmovie/{category}")
//    @FormUrlEncoded
//    Call<Pagination> GetMoviesbyCategory(@Path("category") String category, @Field("page") int page);
//
//    @POST("relatedmovies")
//    @FormUrlEncoded
//    Call<List<ListMovie>> GetRelatedMovies(@Field("id") Integer movie_id);
//
//    @POST("moviesbygenre")
//    @FormUrlEncoded
//    Call<List<Movie>> GetMoviesbyGenre(@Field("id") Integer genre_id);
//
//    @POST("commentsbymovie")
//    @FormUrlEncoded
//    Call<CommentPagination> GetCommentsbyMovie(@Field("id") Integer movie_id, @Field("page") int page);
//
//    @POST("allmoviesbygenres")
//    @FormUrlEncoded
//    Call<Pagination> GetAllMoviesbyGenre(@Field("id") Integer genre_id, @Field("page") int page);
//
//    @POST("movie")
//    @FormUrlEncoded
//    Call<Movie> GetMoviebyId(@Field("id") Integer movie_id);
//
//    @POST("video")
//    @FormUrlEncoded
//    Call<Video> GetVideobyId(@Field("id") Integer video_id);

//    @POST("actorsbymovie")
//    @FormUrlEncoded
//    Call<List<Actor>> GetActorsbyMovie(@Field("id") Integer movie_id);

    @POST("moviesbyactor")
    @FormUrlEncoded
    Call<List<Movie>> GetMoviesbyActor(@Field("id") Integer actor_id);

    @POST("directorsbymovie")
    @FormUrlEncoded
    Call<List<Director>> GetDirectorbyMovie(@Field("id") Integer movie_id);

    @POST("genresbymovie")
    @FormUrlEncoded
    Call<List<Genre>> GetGenresbyMovie(@Field("id") Integer movie_id);

    @POST("searchmovie")
    @FormUrlEncoded
    Call<Pagination> SearchMovie(@Field("key") String key, @Field("page") int page);

//    @POST("login")
//    @FormUrlEncoded
//    Call<Auth> Login(@Field("email") String email, @Field("password") String password);
//
//    @POST("checkemail")
//    @FormUrlEncoded
//    Call<Boolean> CheckEmail(@Field("email") String email);
//
//    @POST("register")
//    @FormUrlEncoded
//    Call<Auth> Register(@Field("name") String name, @Field("email") String email, @Field("password") String password);
//
//    @GET("checktoken")
//    Call<Status> CheckToken(@Header("Authorization") String authHeader);
//
//    @GET("logout")
//    Call<Status> Logout(@Header("Authorization") String authHeader);

//    @POST("storecomment")
//    @FormUrlEncoded
//    Call<Status> SaveComment(@Header("Authorization") String authHeader, @Field("movie_id") int movie_id, @Field("user_id") int user_id, @Field("content") String content);

//    @POST("moviesbyuser")
//    @FormUrlEncoded
//    Call<List<Movie>> GetMoivesbyUser(@Header("Authorization") String authHeader, @Field("id") int user_id);

    @POST("historyofuser")
    @FormUrlEncoded
    Call<List<UserView>> GetHistoryofUser(@Header("Authorization") String authHeader, @Field("id") int user_id);

//    @POST("checkusermovie")
//    @FormUrlEncoded
//    Call<Status> CheckMoviebyUser(@Header("Authorization") String authHeader, @Field("user_id") int user_id, @Field("movie_id") int movie_id);
//
//    @POST("storeusermovie")
//    @FormUrlEncoded
//    Call<Status> SaveUerMovie(@Header("Authorization") String authHeader, @Field("user_id") int user_id, @Field("movie_id") int movie_id);
//
//    @POST("deleteusermovie")
//    @FormUrlEncoded
//    Call<Status> DeleteUerMovie(@Header("Authorization") String authHeader, @Field("user_id") int user_id, @Field("movie_id") int movie_id);

    @POST("deleteuserview")
    @FormUrlEncoded
    Call<Status> DeleteUerView(@Header("Authorization") String authHeader, @Field("user_id") int user_id, @Field("movie_id") int movie_id);

    @POST("storeuserview")
    @FormUrlEncoded
    Call<Status> SaveUerView(@Header("Authorization") String authHeader, @Field("user_id") int user_id, @Field("movie_id") int movie_id, @Field("episode_id") int episode_id);
}
