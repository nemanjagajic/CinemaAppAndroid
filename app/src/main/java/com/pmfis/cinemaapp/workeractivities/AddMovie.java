package com.pmfis.cinemaapp.workeractivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pmfis.cinemaapp.R;
import com.pmfis.cinemaapp.retrofit.APIClient;
import com.pmfis.cinemaapp.retrofit.APIInterface;
import com.pmfis.cinemaapp.retrofit.pojo.MovieRequest;
import com.pmfis.cinemaapp.retrofit.pojo.MovieResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMovie extends AppCompatActivity {

    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        apiInterface = APIClient.getClient().create(APIInterface.class);
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

    public void onAddMovieClicked(View view) {
        String title = ((EditText)findViewById(R.id.et_title)).getText().toString();
        String genre = ((EditText)findViewById(R.id.et_genre)).getText().toString();
        String time = ((EditText)findViewById(R.id.et_time)).getText().toString();
        String imdbLink = ((EditText)findViewById(R.id.et_imdb_link)).getText().toString();
        String priceText = ((EditText)findViewById(R.id.et_price)).getText().toString();

        if (title.equals("") || genre.equals("") || time.equals("") || priceText.equals("")) {
            Toast.makeText(getBaseContext(), "Fill required fields", Toast.LENGTH_SHORT).show();
        } else {
            int price;
            try {
                price = Integer.parseInt(priceText);
            } catch (NumberFormatException nfe) {
                Toast.makeText(getBaseContext(), "Price must be a number", Toast.LENGTH_SHORT).show();
                return;
            }
            MovieRequest movieRequest = new MovieRequest(title, genre, time, imdbLink, price);

            Call<MovieResponse> call = apiInterface.addMovie(movieRequest);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    Toast.makeText(getBaseContext(), "Movie added", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }
}
