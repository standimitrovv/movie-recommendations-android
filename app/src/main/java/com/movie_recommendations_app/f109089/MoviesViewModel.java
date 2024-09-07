package com.movie_recommendations_app.f109089;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private MovieDatabaseHelper dbHelper;

    private MutableLiveData<List<Movie>> allMovies;
    private MutableLiveData<List<Movie>> favoriteMovies;
    private MutableLiveData<List<Movie>> recommendedMovies;

    public MoviesViewModel(Application application) {
        super(application);
        dbHelper = new MovieDatabaseHelper(application);

        allMovies = new MutableLiveData<>();
        favoriteMovies = new MutableLiveData<>();
        recommendedMovies = new MutableLiveData<>();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return this.allMovies;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return this.favoriteMovies;
    }

    public LiveData<List<Movie>> getRecommendedMovies() { return recommendedMovies; }

    public void setAllMovies(List<Movie> movies) {
        allMovies.setValue(movies);
    }

    public void setRecommendedMovies(List<Movie> movies) {
        this.recommendedMovies.setValue(movies);
    }

    public void loadFavoriteMovies() {
        List<Movie> favoriteMovies = dbHelper.getAllFavoriteMovies();
        this.favoriteMovies.setValue(favoriteMovies);
    }

    public void addMovieToFavorites(Movie movie) {
        movie.setIsFavorite(true);
        dbHelper.addFavoriteMovie(movie);
        loadFavoriteMovies();
    }

    public void removeFromFavorites(String movieId) {
        dbHelper.removeFavoriteMovie(movieId);
        loadFavoriteMovies();
    }
}