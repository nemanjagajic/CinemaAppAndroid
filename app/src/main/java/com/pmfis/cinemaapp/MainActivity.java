package com.pmfis.cinemaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pmfis.cinemaapp.authentication.LoginForm;
import com.pmfis.cinemaapp.authentication.RegisterForm;
import com.pmfis.cinemaapp.clientactivities.Movies;
import com.pmfis.cinemaapp.workeractivities.WorkerPanel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLogInClicked(View view) {
        startActivity(new Intent(this, LoginForm.class));
    }

    public void onRegisterHomeButtonClicked(View view) {
        startActivity(new Intent(this, RegisterForm.class));
    }

    public void onLogInAsGuestClicked(View view) {
        Toast.makeText(getBaseContext(), "Logged in as guest", Toast.LENGTH_SHORT).show();
        Movies.loggedPersonId = -1;
        startActivity(new Intent(this, Movies.class));
    }
}
