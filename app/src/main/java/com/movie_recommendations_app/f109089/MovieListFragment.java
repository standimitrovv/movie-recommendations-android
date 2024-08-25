package com.movie_recommendations_app.f109089;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements MovieListAdapter.OnMovieClickListener {

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
        adapter = new MovieListAdapter(movieList, this);
        recyclerView.setAdapter(adapter);

        loadMovies();

        return view;
    }

    private void loadMovies() {
        new Thread(() -> {
            MovieApiClient client = new MovieApiClient();
            String response = client.getPopularMovies();
            List<Movie> movies = MovieParser.parseMovies(response);
            getActivity().runOnUiThread(() -> {
                movieList.clear();
                movieList.addAll(movies);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    @Override
    public void onMovieClick(Movie movie) {
        // Starts MovieDetailsActivity with the selected film
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra("MOVIE_ID", movie.getId());
        startActivity(intent);
    }
}