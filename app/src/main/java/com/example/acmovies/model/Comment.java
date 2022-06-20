package com.example.acmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("movie_id")
    @Expose
    private Integer movieId;
    @SerializedName("parent_id")
    @Expose
    private Object parentId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("user")
    @Expose
    private User user;

    public Integer getId() {
    return id;
    }

    public void setId(Integer id) {
    this.id = id;
    }

    public Integer getUserId() {
    return userId;
    }

    public void setUserId(Integer userId) {
    this.userId = userId;
    }

    public Integer getMovieId() {
    return movieId;
    }

    public void setMovieId(Integer movieId) {
    this.movieId = movieId;
    }

    public Object getParentId() {
    return parentId;
    }

    public void setParentId(Object parentId) {
    this.parentId = parentId;
    }

    public String getContent() {
    return content;
    }

    public void setContent(String content) {
    this.content = content;
    }

    public String getCreatedAt() {
    return createdAt;
    }

    public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
    return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
    return user;
    }

    public void setUser(User user) {
    this.user = user;
    }

}