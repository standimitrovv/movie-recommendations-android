package com.movie_recommendations_app.f109089;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class RecommendedMoviesFragment extends Fragment {
    private MovieListAdapter adapter;
    private MoviesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommended_movies_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_recommended_movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel = new ViewModelProvider(requireActivity()).get(MoviesViewModel.class);
        adapter = new MovieListAdapter(viewModel, getContext());
        recyclerView.setAdapter(adapter);

        viewModel.getRecommendedMovies().observe(getViewLifecycleOwner(), recommendedMovies -> {
            adapter.setMovies(recommendedMovies);
        });
        this.loadMovies();

        return view;
    }

    private void loadMovies() {
        this.viewModel.loadFavoriteMovies();

        List<Movie> favs = this.viewModel.getFavoriteMovies().getValue();
        if(favs == null || favs.isEmpty()) {
            return;
        }

        String genres = this.getGenreIds(favs);

        new Thread(() -> {
            MovieApiClient apiClient = new MovieApiClient();
            apiClient.getMoviesByGenre(genres, new ApiCallback() {
                @Override
                public void onSuccess(List<Movie> movies) {
                        if(movies == null || movies.isEmpty()) { return; }

                        getActivity().runOnUiThread(() -> {
                            viewModel.setRecommendedMovies(movies);
                            adapter.notifyDataSetChanged();
                        });
                }

                @Override
                public void onFailure(String body) {
                    Log.e("FAIL", "The recommended movies failed to fetch: " + body);
                    showToast("Failed to fetch data");
                }
            });
        }).start();
    }

    private void showToast(final String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }

    private String getGenreIds(List<Movie> movies) {
        StringBuilder genreIds = new StringBuilder();

        for (int i = 0; i < movies.size(); i++) {
            genreIds.append(movies.get(i).getGenreId());

            if (i < movies.size() - 1) {
                genreIds.append("|");
            }
        }

        return genreIds.toString();
    }
}
