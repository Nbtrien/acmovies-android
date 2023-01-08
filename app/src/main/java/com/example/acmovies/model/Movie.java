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
    private String avgrating;
    @SerializedName("view")
    @Expose
    private Integer view;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("profileimage")
    @Expose
    private String profileimage;
    @SerializedName("coverimage")
    @Expose
    private String coverimage;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;
    @SerializedName("casts")
    @Expose
    private List<Actor> actors = null;
    @SerializedName("directors")
    @Expose
    private List<Director> directors = null;
    @SerializedName("newepisode")
    @Expose
    private Integer newepisode;
    @SerializedName("episodes")
    @Expose
    private Integer episodes;

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

    public String getAvgrating() {
        return avgrating;
    }

    public void setAvgrating(String avgrating) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getCoverimage() {
        return coverimage;
    }

    public void setCoverimage(String coverimage) {
        this.coverimage = coverimage;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setCasts(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public Integer getNewepisode() {
        return newepisode;
    }

    public void setNewepisode(Integer newepisode) {
        this.newepisode = newepisode;
    }

    public Integer getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Integer episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "MovieClone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", engName='" + engName + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", studio='" + studio + '\'' +
                ", releasedate='" + releasedate + '\'' +
                ", runtime=" + runtime +
                ", avgrating='" + avgrating + '\'' +
                ", view=" + view +
                ", categoryId=" + categoryId +
                ", category='" + category + '\'' +
                ", countryId=" + countryId +
                ", country='" + country + '\'' +
                ", profileimage='" + profileimage + '\'' +
                ", coverimage='" + coverimage + '\'' +
                ", genres=" + genres +
                ", actors=" + actors +
                ", directors=" + directors +
                ", newepisode=" + newepisode +
                ", episodes=" + episodes +
                '}';
    }
}