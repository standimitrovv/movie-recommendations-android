package com.movie_recommendations_app.f109089;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class MovieListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieListAdapter adapter;

    private MoviesViewModel viewModel;

    public MovieListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel = new ViewModelProvider(requireActivity()).get(MoviesViewModel.class);

        adapter = new MovieListAdapter(viewModel);
        recyclerView.setAdapter(adapter);

        viewModel.getAllMovies().observe(getViewLifecycleOwner(), movies -> {
            adapter.setMovies(movies);
        });

        this.loadMovies();

        return view;
    }

    private void loadMovies() {
        new Thread(() -> {
            MovieApiClient apiClient = new MovieApiClient();
            apiClient.getPopularMovies(new ApiCallback() {
                @Override
                public void onSuccess(List<Movie> movies) {
                    if(movies == null || movies.isEmpty()) { return; }

                    getActivity().runOnUiThread(() -> {
                        viewModel.setAllMovies(movies);
                        adapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onFailure(String body) {
                    Log.e("FAIL", "The popular movies failed to fetch: " + body);
                    showToast("Failed to fetch data");
                }
            });
        }).start();
    }

    private void showToast(final String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }
}