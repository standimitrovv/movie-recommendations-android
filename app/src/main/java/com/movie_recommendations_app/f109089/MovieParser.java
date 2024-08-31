package com.movie_recommendations_app.f109089;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MovieParser {

    public static List<Movie> parseMovies(String jsonResponse) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray results = jsonObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                String id = movieJson.getString("id");
                String title = movieJson.getString("title");
                String overview = movieJson.getString("overview");
                String posterUrl = movieJson.getString("poster_path");

                Movie movie = new Movie(
                        id,
                        title,
                        overview,
                        "https://image.tmdb.org/t/p/original"+ posterUrl,
                        false
                );
                movies.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
}

