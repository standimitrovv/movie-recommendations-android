package com.movie_recommendations_app.f109089;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

interface ApiCallback {
    void onSuccess(List<Movie> movies);
    void onFailure(String body);
}

public class MovieApiClient {
    private static final String AUTH_TOKEN = BuildConfig.AUTH_TOKEN;

    public void getPopularMovies(int page, final ApiCallback apiCallback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/popular?language=en-US&page=" + page)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + AUTH_TOKEN)
                .build();

        try (Response res = client.newCall(request).execute()) {
            if(res.body() == null) {
                apiCallback.onFailure("Body is empty");
                return;
            }

            if(res.isSuccessful()) {
                apiCallback.onSuccess(MovieParser.parseMovies(res.body().string()));
            } else {
                apiCallback.onFailure(res.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMoviesByGenre(String genres, final ApiCallback apiCallback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&with_genres=" + genres)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + AUTH_TOKEN)
                .build();

        try(Response res = client.newCall(request).execute()){
            if(res.body() == null) {
                apiCallback.onFailure("Body is empty");
                return;
            }

            if(res.isSuccessful()) {
                apiCallback.onSuccess(MovieParser.parseMovies(res.body().string()));
            } else {
                apiCallback.onFailure(res.body().string());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
