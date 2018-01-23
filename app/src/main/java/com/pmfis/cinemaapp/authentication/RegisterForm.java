package com.pmfis.cinemaapp.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pmfis.cinemaapp.R;
import com.pmfis.cinemaapp.retrofit.APIClient;
import com.pmfis.cinemaapp.retrofit.APIInterface;
import com.pmfis.cinemaapp.retrofit.pojo.PersonRequest;
import com.pmfis.cinemaapp.retrofit.pojo.PersonResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterForm extends AppCompatActivity {

    private APIInterface apiInterface;
    private EditText usernameEt;
    private EditText passwordEt;
    private EditText roleEt;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);

        usernameEt = (EditText) findViewById(R.id.et_register_username);
        passwordEt = (EditText) findViewById(R.id.et_register_password);
        roleEt = (EditText) findViewById(R.id.et_register_role);
        progressBar = (ProgressBar) findViewById(R.id.register_pb);
        progressBar.setVisibility(View.INVISIBLE);

        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    // Called when register button is clicked
    public void onRegisterClicked(View view) {
        final String username = usernameEt.getText().toString();
        final String password = passwordEt.getText().toString();
        final String role = roleEt.getText().toString();

        Call<PersonResponse> call = apiInterface.getPersonByUsername(username);

        if (username.equals("") || password.equals("") || role.equals("")) {
            Toast.makeText(getBaseContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
        } else if (!role.equals("client") && !role.equals("worker") && !role.equals("admin")) {
            Toast.makeText(getBaseContext(), "Role must be: client, worker or admin", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            call.enqueue(new Callback<PersonResponse>() {
                @Override
                public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                    if (response.body().getId() != -1) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        createPerson(new PersonRequest(
                                username,
                                password,
                                role
                        ));
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<PersonResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }

    // Helper method that create new person
    private void createPerson(PersonRequest personRequest) {
        Call<PersonResponse> call = apiInterface.addPerson(personRequest);

        call.enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
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
}
