package com.pmfis.cinemaapp.retrofit.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieRequest {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("imdbLink")
    @Expose
    private String imdbLink;
    @SerializedName("gradeSum")
    @Expose
    private Integer gradeSum;
    @SerializedName("gradeNum")
    @Expose
    private Integer gradeNum;
    @SerializedName("numberOfPersons")
    @Expose
    private Integer numberOfPersons;
    @SerializedName("price")
    @Expose
    private Integer price;

    public MovieRequest(String title, String genre, String startTime, String imdbLink, Integer price) {
        this.title = title;
        this.genre = genre;
        this.startTime = startTime;
        this.imdbLink = imdbLink;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getImdbLink() {
        return imdbLink;
    }

    public void setImdbLink(String imdbLink) {
        this.imdbLink = imdbLink;
    }

    public Integer getGradeSum() {
        return gradeSum;
    }

    public void setGradeSum(Integer gradeSum) {
        this.gradeSum = gradeSum;
    }

    public Integer getGradeNum() {
        return gradeNum;
    }

    public void setGradeNum(Integer gradeNum) {
        this.gradeNum = gradeNum;
    }

    public Integer getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(Integer numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
