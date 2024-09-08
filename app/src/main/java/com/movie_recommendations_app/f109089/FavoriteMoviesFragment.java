package com.movie_recommendations_app.f109089;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteMoviesFragment extends Fragment {

    private MovieListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_movies_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_favorite_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        MoviesViewModel viewModel = new ViewModelProvider(this).get(MoviesViewModel.class);

        adapter = new MovieListAdapter(viewModel, getContext());
        recyclerView.setAdapter(adapter);

        viewModel.getFavoriteMovies().observe(getViewLifecycleOwner(), favoriteMovies -> {
            adapter.setMovies(favoriteMovies);
        });
        viewModel.loadFavoriteMovies();

        return view;
    }
}
