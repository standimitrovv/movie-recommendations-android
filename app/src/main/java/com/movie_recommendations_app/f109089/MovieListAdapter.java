package com.movie_recommendations_app.f109089;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private MovieDatabaseHelper db;

    public MovieListAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.db = new MovieDatabaseHelper(context);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.titleTextView.setText(movie.getTitle());
        holder.titleTextView.setText(movie.getTitle());
        holder.descriptionTextView.setText(movie.getDescription());

        Glide
                .with(holder.itemView.getContext())
                .load(movie.getPosterUrl())
                .into(holder.posterImageView);

        holder.favoriteButton.setImageResource(movie.getIsFavorite() ? R.drawable.icon_heart_filled : R.drawable.icon_heart);

        holder.favoriteButton.setOnClickListener(v -> {
            boolean isFavorite = movie.getIsFavorite();
            movie.setIsFavorite(!isFavorite);

            if (!isFavorite) {
                holder.favoriteButton.setImageResource(R.drawable.icon_heart_filled);
                addToFavorites(movie);
            } else {
                holder.favoriteButton.setImageResource(R.drawable.icon_heart);
                movie.setIsFavorite(false);
                removeFromFavorites(movie.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;
        TextView titleTextView;
        TextView descriptionTextView;
        ImageButton favoriteButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.movie_poster);
            titleTextView = itemView.findViewById(R.id.movie_title);
            descriptionTextView = itemView.findViewById(R.id.movie_description);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
        }
    }

    private void addToFavorites(Movie movie) {
        movie.setIsFavorite(true);
        db.addFavoriteMovie(movie);
    }

    private void removeFromFavorites(String movieId){
        db.removeFavoriteMovie(movieId);
    }
}