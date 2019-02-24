package com.raphael.carvalho.android.popularmovies.movies.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.raphael.carvalho.android.popularmovies.R;
import com.raphael.carvalho.android.popularmovies.movies.MoviesUrl;
import com.raphael.carvalho.android.popularmovies.movies.adapter.MoviesAdapter;
import com.raphael.carvalho.android.popularmovies.movies.model.MovieInfo;
import com.raphael.carvalho.android.popularmovies.movies.task.SearchMoviesTask;
import com.raphael.carvalho.android.popularmovies.util.TaskListener;

public class MoviesActivity extends AppCompatActivity implements TaskListener<MovieInfo> {
    private View pbLoading;
    private View cgErrorLoading;

    private MoviesAdapter adapter;
    private RecyclerView rvMovies;

    private String sortBy;
    private MovieInfo movieInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        sortBy = MoviesUrl.SORT_BY_POPULARITY;
        initViews();
        new SearchMoviesTask(MoviesActivity.this).execute(sortBy, "1");
    }

    private void initViews() {
        pbLoading = findViewById(R.id.pb_loading);

        cgErrorLoading = findViewById(R.id.cg_error_loading);
        findViewById(R.id.bt_error_loading).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String page = (movieInfo != null) ? movieInfo.getPage() : "1";

                        new SearchMoviesTask(MoviesActivity.this)
                                .execute(sortBy, page);
                    }
                }
        );

        adapter = new MoviesAdapter();
        rvMovies = findViewById(R.id.rv_movies);
        rvMovies.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
        cgErrorLoading.setVisibility(View.GONE);
        rvMovies.setVisibility(View.GONE);

        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage(AsyncTask task) {
        pbLoading.setVisibility(View.GONE);
        rvMovies.setVisibility(View.GONE);

        cgErrorLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public synchronized void showResult(MovieInfo result) {
        pbLoading.setVisibility(View.GONE);
        cgErrorLoading.setVisibility(View.GONE);

        rvMovies.setVisibility(View.VISIBLE);

        movieInfo = result;
        adapter.addMovies(result.getMovies());
    }
}
