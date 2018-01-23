package com.pmfis.cinemaapp.clientactivities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pmfis.cinemaapp.R;
import com.pmfis.cinemaapp.retrofit.APIClient;
import com.pmfis.cinemaapp.retrofit.APIInterface;
import com.pmfis.cinemaapp.retrofit.pojo.MovieResponse;
import com.pmfis.cinemaapp.retrofit.pojo.ReservationRequest;
import com.pmfis.cinemaapp.retrofit.pojo.ReservationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reservation extends AppCompatActivity {

    private TextView title;
    private TextView genre;
    private TextView time;
    private TextView imdbLink;
    private TextView grade;
    private TextView price;
    private ProgressBar progressBar;
    private APIInterface apiInterface;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        // Setting movie
        movieId = Integer.parseInt(getIntent().getStringExtra("EXTRA_SESSION_ID"));

        // Finding all TextViews
        title = (TextView) findViewById(R.id.reservation_movie_title);
        genre = (TextView) findViewById(R.id.reservation_movie_genre);
        time = (TextView) findViewById(R.id.reservation_movie_time);
        imdbLink = (TextView) findViewById(R.id.reservation_movie_imdb_link);
        grade = (TextView) findViewById(R.id.reservation_movie_grade);
        price = (TextView) findViewById(R.id.reservation_movie_price);

        // Setting progress bar
        progressBar = (ProgressBar) findViewById(R.id.reservation_pb);
        progressBar.setVisibility(View.INVISIBLE);

        fillTextViews();
    }

    private void fillTextViews() {
        Call<MovieResponse> call = apiInterface.getMovieById(movieId);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movie = response.body();

                // Calculate grade
                double gradeCalculated = 0;
                if (movie.getGradeNum() != 0) {
                    gradeCalculated = movie.getGradeSum() / movie.getGradeNum();
                }

                // Fill fields
                title.setText(movie.getTitle());
                genre.setText("Genre: " + movie.getGenre());
                time.setText("Time: " + movie.getStartTime());
                imdbLink.setText("Imdb link: " + movie.getImdbLink());
                grade.setText("Grade " + gradeCalculated);
                price.setText("Price: " + movie.getPrice());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void onMakeReservationClicked(View view) {
        final ReservationRequest reservationRequest = new ReservationRequest(movieId, Movies.loggedPersonId);

        // Dialog
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        makeReservation(reservationRequest);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(getBaseContext(), "Action dismissed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Make reservation for this movie?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void makeReservation(ReservationRequest reservationRequest) {
        progressBar.setVisibility(View.VISIBLE);
        Call<ReservationResponse> call = apiInterface.makeReservation(reservationRequest);
        call.enqueue(new Callback<ReservationResponse>() {
            @Override
            public void onResponse(Call<ReservationResponse> call, Response<ReservationResponse> response) {
                Toast.makeText(getBaseContext(), "Reservation successfully made", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ReservationResponse> call, Throwable t) {
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

}
