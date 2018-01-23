package com.pmfis.cinemaapp.retrofit.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReservationRequest {

    @SerializedName("idMovie")
    @Expose
    private int idMovie;
    @SerializedName("idPerson")
    @Expose
    private int idPerson;

    public ReservationRequest(int idMovie, int idPerson) {
        this.idMovie = idMovie;
        this.idPerson = idPerson;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }
}
