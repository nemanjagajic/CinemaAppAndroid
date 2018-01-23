package com.pmfis.cinemaapp.workeractivities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pmfis.cinemaapp.R;
import com.pmfis.cinemaapp.adapter.MovieAdapter;
import com.pmfis.cinemaapp.adapter.ReservationAdapter;
import com.pmfis.cinemaapp.model.Movie;
import com.pmfis.cinemaapp.retrofit.APIClient;
import com.pmfis.cinemaapp.retrofit.APIInterface;
import com.pmfis.cinemaapp.retrofit.pojo.MovieResponse;
import com.pmfis.cinemaapp.retrofit.pojo.PersonResponse;
import com.pmfis.cinemaapp.retrofit.pojo.ReservationResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieReservations extends AppCompatActivity {

    private ArrayList<String> reservations;
    private ReservationAdapter adapter;
    private ListView listView;
    private APIInterface apiInterface;
    private ProgressBar progressBar;
    private int movieId;

    private TextView movieTitle;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reservations);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        // Setting movieId
        movieId = Integer.parseInt(getIntent().getStringExtra("EXTRA_SESSION_ID"));

        // Initializing TextViews
        movieTitle = (TextView) findViewById(R.id.movie_reservations_title_movie);
        time = (TextView) findViewById(R.id.movie_reservations_time);
        setMovieInformation();

        // Setting ListView
        reservations = new ArrayList<>();
        adapter = new ReservationAdapter(this, reservations);
        listView = (ListView) findViewById(R.id.movie_reservations_list);
        listView.setAdapter(adapter);

        // Progress bar
        progressBar = (ProgressBar) findViewById(R.id.movie_reservations_pb);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshList();
    }

    // Shows all reservations
    private void showAllReservations() {
        Call<List<PersonResponse>> call  = apiInterface.getPersonsWhoReservedMovie(movieId);
        call.enqueue(new Callback<List<PersonResponse>>() {
            @Override
            public void onResponse(Call<List<PersonResponse>> call, Response<List<PersonResponse>> response) {
                List<PersonResponse> responseList = response.body();
                int numberOfPersons = responseList.size();

                for (int i = 0; i < numberOfPersons; i++) {
                    reservations.add(responseList.get(i).getUsername() + "," + responseList.get(i).getId());
                }

                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<PersonResponse>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    // Called when delete reservation button is clicked
    public void onDeleteReservationClicked(View view) {
        final int personId = Integer.parseInt(view.getContentDescription().toString());

        // Dialog
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteReservation(personId);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(getBaseContext(), "Action dismissed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Are you sure you want to delete this reservation?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    // Helper method that deletes reservation
    private void deleteReservation(int personId) {
        Call<ReservationResponse> call = apiInterface.deleteReservation(movieId, personId);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response) {
                Toast.makeText(getBaseContext(), "Reservation deleted", Toast.LENGTH_SHORT);
                refreshList();
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t) {

            }
        });
    }

    // Helper method that fills TextViews with information about movie
    private void setMovieInformation() {
        Call<MovieResponse> call = apiInterface.getMovieById(movieId);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movieTitle.setText(response.body().getTitle());
                time.setText(response.body().getStartTime());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // App icon in action bar clicked; goto parent activity
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshList() {
        reservations.clear();
        progressBar.setVisibility(View.VISIBLE);
        showAllReservations();
    }
}
