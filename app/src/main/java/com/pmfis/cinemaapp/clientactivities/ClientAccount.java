package com.pmfis.cinemaapp.clientactivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pmfis.cinemaapp.R;
import com.pmfis.cinemaapp.retrofit.APIClient;
import com.pmfis.cinemaapp.retrofit.APIInterface;
import com.pmfis.cinemaapp.retrofit.pojo.MovieResponse;
import com.pmfis.cinemaapp.retrofit.pojo.PersonResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientAccount extends AppCompatActivity {

    private ArrayList<String> reservations;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    private TextView username;
    private ProgressBar progressBar;

    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_account);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        username = (TextView) findViewById(R.id.user_username);
        progressBar = (ProgressBar) findViewById(R.id.user_pb);

        // Setting ListView
        reservations = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, reservations) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(getResources().getColor(R.color.colorWhite));
                return tv;
            }
        };
        listView = (ListView) findViewById(R.id.user_reservation_list);
        listView.setAdapter(adapter);

        setUsername();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshList();
    }

    private void showAllReservations() {
        Call<List<MovieResponse>> call = apiInterface.getMoviesForPerson(Movies.loggedPersonId);
        call.enqueue(new Callback<List<MovieResponse>>() {
            @Override
            public void onResponse(Call<List<MovieResponse>> call, Response<List<MovieResponse>> response) {
                List<MovieResponse> movies = response.body();
                int numberOfMovies = movies.size();

                for (int i = 0; i < numberOfMovies; i++) {
                    MovieResponse currentMovie = movies.get(i);

                    String reservation = "\ntitle: " + currentMovie.getTitle() + "\ngenre: " +
                            currentMovie.getGenre() + "\ntime: " + currentMovie.getStartTime() +
                            "\nprice: " + currentMovie.getPrice() + "\n";
                    reservations.add(reservation);
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

    private void setUsername() {
        Call<PersonResponse> call = apiInterface.getPersonById(Movies.loggedPersonId);
        call.enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                username.setText(response.body().getUsername());
            }

            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) {
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
