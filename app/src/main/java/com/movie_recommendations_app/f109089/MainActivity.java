package com.movie_recommendations_app.f109089;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            MovieListFragment movieListFragment = new MovieListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main, movieListFragment);
            fragmentTransaction.commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();

            if (id == R.id.navigation_all_movies) {
                selectedFragment = new MovieListFragment();
            } else if (id == R.id.navigation_favorites) {
                selectedFragment = new FavoriteMoviesFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main, selectedFragment)
                        .commit();
            }

            return true;
        });

        Intent serviceIntent = new Intent(this, MovieUpdateService.class);
        startService(serviceIntent);
    }
}