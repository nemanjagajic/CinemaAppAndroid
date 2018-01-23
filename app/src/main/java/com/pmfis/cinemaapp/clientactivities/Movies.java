package com.pmfis.cinemaapp.clientactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.pmfis.cinemaapp.R;
import com.pmfis.cinemaapp.adapter.MovieAdapter;
import com.pmfis.cinemaapp.model.Movie;
import com.pmfis.cinemaapp.retrofit.APIClient;
import com.pmfis.cinemaapp.retrofit.APIInterface;
import com.pmfis.cinemaapp.retrofit.pojo.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Movies extends AppCompatActivity {

    public static int loggedPersonId;

    private ArrayList<Movie> movies;
    private MovieAdapter adapter;
    private ListView listView;
    private APIInterface apiInterface;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        // Setting ListView
        movies = new ArrayList<>();
        adapter = new MovieAdapter(this, movies);
        listView = (ListView) findViewById(R.id.movies_list);
        listView.setAdapter(adapter);

        // Progress bar
        progressBar = (ProgressBar) findViewById(R.id.movies_pb);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshList();
    }

    // Show all movies
    private void showAllMovies() {
        Call<List<MovieResponse>> call = apiInterface.getAllMovies();

        call.enqueue(new Callback<List<MovieResponse>>() {
            @Override
            public void onResponse(Call<List<MovieResponse>> call, Response<List<MovieResponse>> response) {
                int numberOfMovies = response.body().size();
                List<MovieResponse> responseList = response.body();

                for (int i = 0; i < numberOfMovies; i++) {
                    MovieResponse currentMovie = responseList.get(i);

                    movies.add(new Movie(
                            currentMovie.getId(),
                            currentMovie.getTitle(),
                            currentMovie.getGenre(),
                            currentMovie.getStartTime(),
                            currentMovie.getImdbLink(),
                            false,
                            currentMovie.getPrice()
                    ));
                }

                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<MovieResponse>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void onMoreAboutMovieClicked(View view) {
        startActivity(new Intent(this, Reservation.class).putExtra("EXTRA_SESSION_ID",
                view.getContentDescription() + ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // App icon in action bar clicked; goto parent activity
                this.finish();
                return true;
            case R.id.refresh_item_movies:
                refreshList();
                return true;
            case R.id.account_item:
                startActivity(new Intent(this, ClientAccount.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshList() {
        movies.clear();
        progressBar.setVisibility(View.VISIBLE);
        showAllMovies();
    }
}
