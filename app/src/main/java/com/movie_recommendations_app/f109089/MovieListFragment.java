package com.movie_recommendations_app.f109089;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class MovieListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieListAdapter adapter;

    private MoviesViewModel viewModel;

    private ProgressBar progressBar;

    private boolean isLoading = false;
    private int currentPage = 1;
    private final int PAGE_SIZE = 20;

    public MovieListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.progress_bar);

        viewModel = new ViewModelProvider(requireActivity()).get(MoviesViewModel.class);

        adapter = new MovieListAdapter(viewModel);
        recyclerView.setAdapter(adapter);

        viewModel.getAllMovies().observe(getViewLifecycleOwner(), movies -> {
            adapter.setMovies(movies);
        });

        this.loadMovies(false);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                boolean isAtPageBottom = (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE;

                if (!isLoading && isAtPageBottom) {
                    loadMovies(true);
                }
            }
        });

        return view;
    }

    private void loadMovies(boolean more) {
        if(more) {
            isLoading = true;
            currentPage++;
            progressBar.setVisibility(View.VISIBLE);
        }

        new Thread(() -> {
            MovieApiClient apiClient = new MovieApiClient();
            apiClient.getPopularMovies(currentPage, new ApiCallback() {
                @Override
                public void onSuccess(List<Movie> movies) {
                    if(movies == null || movies.isEmpty()) { return; }

                    getActivity().runOnUiThread(() -> {
                        viewModel.setAllMovies(movies);
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                        progressBar.setVisibility(View.GONE);
                    });
                }

                @Override
                public void onFailure(String body) {
                    isLoading = false;
                    Log.e("FAIL", "The popular movies failed to fetch: " + body);
                    showToast("Failed to fetch data");
                    progressBar.setVisibility(View.GONE);
                }
            });
        }).start();
    }

    private void showToast(final String message) {
        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }
}