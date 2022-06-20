package com.example.acmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Director {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("story")
    @Expose
    private String story;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("image_id")
    @Expose
    private Integer imageId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
    return id;
    }

    public void setId(Integer id) {
    this.id = id;
    }

    public String getName() {
    return name;
    }

    public void setName(String name) {
    this.name = name;
    }

    public String getBirthday() {
    return birthday;
    }

    public void setBirthday(String birthday) {
    this.birthday = birthday;
    }

    public String getGender() {
    return gender;
    }

    public void setGender(String gender) {
    this.gender = gender;
    }

    public String getStory() {
    return story;
    }

    public void setStory(String story) {
    this.story = story;
    }

    public Integer getCountryId() {
    return countryId;
    }

    public void setCountryId(Integer countryId) {
    this.countryId = countryId;
    }

    public Integer getImageId() {
    return imageId;
    }

    public void setImageId(Integer imageId) {
    this.imageId = imageId;
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

}