package com.example.acmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("eng_name")
    @Expose
    private String engName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("studio")
    @Expose
    private String studio;
    @SerializedName("releasedate")
    @Expose
    private String releasedate;
    @SerializedName("runtime")
    @Expose
    private Integer runtime;
    @SerializedName("avgrating")
    @Expose
    private Float avgrating;
    @SerializedName("view")
    @Expose
    private Integer view;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("profileimage_id")
    @Expose
    private Integer profileimageId;
    @SerializedName("coverimage_id")
    @Expose
    private Integer coverimageId;
    @SerializedName("trailer_id")
    @Expose
    private Integer trailerId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("profileimage")
    @Expose
    private Image profileimage;
    @SerializedName("coverimage")
    @Expose
    private Image coverimage;
    @SerializedName("trailer")
    @Expose
    private Trailer trailer;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("listepisode")
    @Expose
    private List<Episode> listepisode = null;
    @SerializedName("country")
    @Expose
    private Country country;
    @SerializedName("category")
    @Expose
    private Category category;

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

    public String getEngName() {
    return engName;
    }

    public void setEngName(String engName) {
    this.engName = engName;
    }

    public String getDescription() {
    return description;
    }

    public void setDescription(String description) {
    this.description = description;
    }

    public String getLanguage() {
    return language;
    }

    public void setLanguage(String language) {
    this.language = language;
    }

    public String getStudio() {
    return studio;
    }

    public void setStudio(String studio) {
    this.studio = studio;
    }

    public String getReleasedate() {
    return releasedate;
    }

    public void setReleasedate(String releasedate) {
    this.releasedate = releasedate;
    }

    public Integer getRuntime() {
    return runtime;
    }

    public void setRuntime(Integer runtime) {
    this.runtime = runtime;
    }

    public Float getAvgrating() {
    return avgrating;
    }

    public void setAvgrating(Float avgrating) {
    this.avgrating = avgrating;
    }

    public Integer getView() {
    return view;
    }

    public void setView(Integer view) {
    this.view = view;
    }

    public Integer getCategoryId() {
    return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
    }

    public Integer getCountryId() {
    return countryId;
    }

    public void setCountryId(Integer countryId) {
    this.countryId = countryId;
    }

    public Integer getProfileimageId() {
    return profileimageId;
    }

    public void setProfileimageId(Integer profileimageId) {
    this.profileimageId = profileimageId;
    }

    public Integer getCoverimageId() {
    return coverimageId;
    }

    public void setCoverimageId(Integer coverimageId) {
    this.coverimageId = coverimageId;
    }

    public Integer getTrailerId() {
    return trailerId;
    }

    public void setTrailerId(Integer trailerId) {
    this.trailerId = trailerId;
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

    public Image getProfileimage() {
    return profileimage;
    }

    public void setProfileimage(Image profileimage) {
    this.profileimage = profileimage;
    }

    public Image getCoverimage() {
    return coverimage;
    }

    public void setCoverimage(Image coverimage) {
    this.coverimage = coverimage;
    }

    public Trailer getTrailer() {
    return trailer;
    }

    public void setTrailer(Trailer trailer) {
    this.trailer = trailer;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<Episode> getListepisode() {
        return listepisode;
    }

    public void setListepisode(List<Episode> listepisode) {
        this.listepisode = listepisode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}