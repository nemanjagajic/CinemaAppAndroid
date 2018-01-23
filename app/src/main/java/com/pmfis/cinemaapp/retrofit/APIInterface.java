package com.pmfis.cinemaapp.retrofit;


import com.pmfis.cinemaapp.retrofit.pojo.MovieRequest;
import com.pmfis.cinemaapp.retrofit.pojo.MovieResponse;
import com.pmfis.cinemaapp.retrofit.pojo.PersonRequest;
import com.pmfis.cinemaapp.retrofit.pojo.PersonResponse;
import com.pmfis.cinemaapp.retrofit.pojo.ReservationRequest;
import com.pmfis.cinemaapp.retrofit.pojo.ReservationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {
    // Movie
    @GET("/movie/getAll")
    Call<List<MovieResponse>> getAllMovies();

    @GET("/movie/getMovie/{id}")
    Call<MovieResponse> getMovieById(@Path("id") int id);

    @POST("/movie/add")
    Call<MovieResponse> addMovie(@Body MovieRequest movieToAdd);

    @DELETE("/movie/delete/{id}")
    Call<MovieResponse> deleteMovie(@Path("id") int id);

    // Person
    @GET("/person/getAll")
    Call<List<PersonResponse>> getAllPersons();

    @GET("/person/get/{id}")
    Call<PersonResponse> getPersonById(@Path("id") int id);

    @GET("/person/getUser/{username}")
    Call<PersonResponse> getPersonByUsername(@Path("username") String username);

    @POST("/person/add")
    Call<PersonResponse> addPerson(@Body PersonRequest personToAdd);

    @DELETE("/person/delete/{id}")
    Call<PersonResponse> deletePerson(@Path("id") int id);

    // Reservation
    @GET("movie/reservation/getMovies/{id}")
    Call<List<MovieResponse>> getMoviesForPerson(@Path("id") int id);

    @POST("/movie/reservation/add")
    Call<ReservationResponse> makeReservation(@Body ReservationRequest reservationRequest);

    @GET("movie/reservation/getPersons/{movieId}")
    Call<List<PersonResponse>> getPersonsWhoReservedMovie(@Path("movieId") int movieId);

    @DELETE("movie/reservation/delete/{idMovie}/{idPerson}")
    Call<ReservationResponse> deleteReservation(@Path("idMovie") int idMovie, @Path("idPerson") int idPerson);
}
