package com.movie_recommendations_app.f109089;

public class Movie {
    private String id;
    private String title;
    private String overview;

    public Movie(String id, String title, String overview) {
        this.id = id;
        this.title = title;
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }
}
