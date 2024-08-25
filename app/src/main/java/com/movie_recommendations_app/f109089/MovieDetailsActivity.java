package com.movie_recommendations_app.f109089;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String movieId = intent.getStringExtra("MOVIE_ID");

            MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.newInstance(movieId);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main, movieDetailsFragment);
            fragmentTransaction.commit();
        }
    }
}