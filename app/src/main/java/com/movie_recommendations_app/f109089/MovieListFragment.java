package com.movie_recommendations_app.f109089;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieListAdapter adapter;
    private List<Movie> movieList = new ArrayList<>();

    public MovieListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MovieListAdapter(movieList);
        recyclerView.setAdapter(adapter);

        loadMovies();

        return view;
    }

    private void loadMovies() {
        new Thread(() -> {
            MovieApiClient apiClient = new MovieApiClient();
            String response = apiClient.getPopularMovies();

            if (response != null) {
                try {
                    List<Movie> movies = MovieParser.parseMovies(response);

                    getActivity().runOnUiThread(() -> {
                        movieList.clear();
                        movieList.addAll(movies);
                        adapter.notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Error parsing movie data");
                }
            } else {
                showToast("Failed to fetch data");
            }
        }).start();
    }

    private void showToast(final String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }
}