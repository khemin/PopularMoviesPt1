package com.khemin.and.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MoviesAdapter mMoviesAdapter;
    private FetchMoviesTask mFetchMoviesTask;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView moviesGridView = (GridView) rootView.findViewById(R.id.gv_movies_grid);
        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) mMoviesAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("movieObject", movie);
                startActivity(intent);
            }
        });
        mMoviesAdapter = new MoviesAdapter(getActivity());
        moviesGridView.setAdapter(mMoviesAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFetchMoviesTask = new FetchMoviesTask(mMoviesAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sorting = prefs.getString(getString(R.string.pref_sorting_key),
                getString(R.string.pref_sorting_popular));
        mFetchMoviesTask.execute(sorting);
    }
}
