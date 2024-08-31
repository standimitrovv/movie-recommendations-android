package com.movie_recommendations_app.f109089;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.URL;

import java.io.BufferedReader;
import java.net.HttpURLConnection;

public class MovieDetailsFragment extends Fragment {

    private static final String ARG_MOVIE_ID = "533535";
    private String movieId;
    private MovieDatabaseHelper databaseHelper;
    private TextView movieTitleTextView;
    private TextView movieOverviewTextView;
    private Button favoriteButton;

    public MovieDetailsFragment() {}

    public static MovieDetailsFragment newInstance(String movieId) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();

        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_ID, movieId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getString(ARG_MOVIE_ID);
        }

        databaseHelper = new MovieDatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        movieTitleTextView = view.findViewById(R.id.movie_title);
        movieOverviewTextView = view.findViewById(R.id.movie_overview);
        favoriteButton = view.findViewById(R.id.favorite_button);

        loadMovieDetails(movieId);

        favoriteButton.setOnClickListener(v -> {
            if (databaseHelper.isFavorite(movieId)) {
                databaseHelper.removeFavoriteMovie(movieId);
                favoriteButton.setText("Add to Favorites");
            } else {
                Movie movie = new Movie(movieId, movieTitleTextView.getText().toString(), movieOverviewTextView.getText().toString(), "", true);
                databaseHelper.addFavoriteMovie(movie);
                favoriteButton.setText("Remove from Favorites");
            }
        });

        return view;
    }

    private void loadMovieDetails(String movieId) {
        new Thread(() -> {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                reader = new BufferedReader(inputStream);
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                getActivity().runOnUiThread(() -> {
                    if (databaseHelper.isFavorite(movieId)) {
                        favoriteButton.setText("Remove from Favorites");
                    } else {
                        favoriteButton.setText("Add to Favorites");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
