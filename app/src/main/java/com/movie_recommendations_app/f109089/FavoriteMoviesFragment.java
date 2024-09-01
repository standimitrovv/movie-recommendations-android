package com.movie_recommendations_app.f109089;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteMoviesFragment extends Fragment {

    private MovieDatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private MovieListAdapter adapter;
    private List<Movie> favoriteMovies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_movies_fragment, container, false);

        dbHelper = new MovieDatabaseHelper(getActivity());
        recyclerView = view.findViewById(R.id.recycler_view_favorite_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadFavoriteMovies();

        return view;
    }

    private void loadFavoriteMovies() {
        favoriteMovies = dbHelper.getAllFavoriteMovies();
        adapter = new MovieListAdapter(favoriteMovies, getActivity());
        recyclerView.setAdapter(adapter);
    }
}
