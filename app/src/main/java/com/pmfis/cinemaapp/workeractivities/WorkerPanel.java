package com.pmfis.cinemaapp.workeractivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class WorkerPanel extends AppCompatActivity {

    private ArrayList<Movie> movies;
    private MovieAdapter adapter;
    private ListView listView;
    private APIInterface apiInterface;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_panel);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        // Setting ListView
        movies = new ArrayList<>();
        adapter = new MovieAdapter(this, movies);
        listView = (ListView) findViewById(R.id.worker_panel_list);
        listView.setAdapter(adapter);

        // Progress bar
        progressBar = (ProgressBar) findViewById(R.id.worker_panel_pb);
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
                            true,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_worker_panel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // App icon in action bar clicked; goto parent activity
                this.finish();
                return true;
            case R.id.refresh_item:
                refreshList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Called when add button is clicked
    public void onMovieFormClicked(View view) {
        startActivity(new Intent(this, AddMovie.class));
    }

    // Called when delete button is clicked
    public void onDeleteMovieClicked(View view) {
        final int id = Integer.parseInt(view.getContentDescription().toString().trim());

        // Dialog
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteMovie(id);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(getBaseContext(), "Action dismissed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Are you sure you want to delete this movie?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void deleteMovie(int id) {
        Call<MovieResponse> call = apiInterface.deleteMovie(id);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Toast.makeText(getBaseContext(), "Movie deleted", Toast.LENGTH_SHORT).show();
                refreshList();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    public void onReservationsMovieClicked(View view) {
        int id = Integer.parseInt(view.getContentDescription().toString().trim());
        startActivity(new Intent(this, MovieReservations.class).putExtra("EXTRA_SESSION_ID", id + ""));
    }

    private void refreshList() {
        movies.clear();
        progressBar.setVisibility(View.VISIBLE);
        showAllMovies();
    }
}
