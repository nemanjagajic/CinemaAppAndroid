package com.pmfis.cinemaapp.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pmfis.cinemaapp.R;
import com.pmfis.cinemaapp.clientactivities.Movies;
import com.pmfis.cinemaapp.retrofit.APIClient;
import com.pmfis.cinemaapp.retrofit.APIInterface;
import com.pmfis.cinemaapp.retrofit.pojo.PersonResponse;
import com.pmfis.cinemaapp.workeractivities.WorkerPanel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginForm extends AppCompatActivity {

    private APIInterface apiInterface;
    private EditText usernameEt;
    private EditText passwordEt;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        usernameEt = (EditText) findViewById(R.id.et_login_username);
        passwordEt = (EditText) findViewById(R.id.et_login_password);
        progressBar = (ProgressBar) findViewById(R.id.login_pb);
        progressBar.setVisibility(View.INVISIBLE);

        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public void onLogInClicked(View view) {
        Call<PersonResponse> call = apiInterface.getPersonByUsername(usernameEt.getText().toString());

        if (usernameEt.getText().toString().equals("") || passwordEt.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Fill both fields", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            call.enqueue(new Callback<PersonResponse>() {
                @Override
                public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                    PersonResponse foundPerson = response.body();
                    if (foundPerson.getId() == -1) { // not found
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), "Username doesn't exist", Toast.LENGTH_SHORT).show();
                    } else if (!foundPerson.getPassword().equals(passwordEt.getText().toString())) { // wrong password
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                    } else if (!foundPerson.getRole().equals("worker")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), "Logged in as client", Toast.LENGTH_SHORT).show();
                        Movies.loggedPersonId = foundPerson.getId();
                        startActivity(new Intent(getBaseContext(), Movies.class));
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), "Logged in as worker", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(), WorkerPanel.class));
                    }
                }

                @Override
                public void onFailure(Call<PersonResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        }
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
