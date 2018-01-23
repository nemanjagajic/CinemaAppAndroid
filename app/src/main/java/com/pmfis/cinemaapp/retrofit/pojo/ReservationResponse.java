package com.pmfis.cinemaapp.retrofit.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReservationResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("idPerson")
    @Expose
    private Integer idPerson;
    @SerializedName("idMovie")
    @Expose
    private Integer idMovie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }

    public Integer getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(Integer idMovie) {
        this.idMovie = idMovie;
    }
}
