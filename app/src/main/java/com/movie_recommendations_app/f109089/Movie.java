package com.movie_recommendations_app.f109089;

public class Movie {
    private String id;
    private String title;
    private String description;
    private String posterUrl;
    private int genreId;
    private boolean isFavorite;

    public Movie(String id, String title, String description, String posterUrl, int genreId, Boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.genreId = genreId;
        this.isFavorite = isFavorite;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
