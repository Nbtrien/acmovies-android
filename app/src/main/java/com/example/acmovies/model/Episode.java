package com.example.acmovies.model;

import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Episode implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("seriesmovie_id")
    @Expose
    private Integer seriesmovieId;
    @SerializedName("episode")
    @Expose
    private Integer episode;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("profileimage_id")
    @Expose
    private Integer profileimageId;
    @SerializedName("video_id")
    @Expose
    private Integer videoId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("image")
    @Expose
    private Image image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeriesmovieId() {
        return seriesmovieId;
    }

    public void setSeriesmovieId(Integer seriesmovieId) {
        this.seriesmovieId = seriesmovieId;
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getProfileimageId() {
        return profileimageId;
    }

    public void setProfileimageId(Integer profileimageId) {
        this.profileimageId = profileimageId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}