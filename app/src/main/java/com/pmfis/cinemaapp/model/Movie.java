package com.pmfis.cinemaapp.model;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private String time;
    private String imdbLink;
    private boolean editable;
    private int price;

    public Movie(int id, String title, String genre, String time, String imdbLink, boolean editable, int price) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.time = time;
        this.imdbLink = imdbLink;
        this.editable = editable;
        this.price = price;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImdbLink() {
        return imdbLink;
    }

    public void setImdbLink(String imdbLink) {
        this.imdbLink = imdbLink;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
