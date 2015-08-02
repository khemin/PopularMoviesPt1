package com.khemin.and.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        TextView tvReleaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        TextView tvRating = (TextView) rootView.findViewById(R.id.tv_rating);
        TextView tvSynopsis = (TextView) rootView.findViewById(R.id.tv_synopsis);
        ImageView ivPoster = (ImageView) rootView.findViewById(R.id.iv_poster);

        Movie movie = (Movie) getActivity().getIntent().getSerializableExtra("movieObject");

        tvTitle.setText(movie.getTitle());
        if (movie.getReleaseDate() != null) {
            tvReleaseDate.setText(SimpleDateFormat.getDateInstance().format(movie.getReleaseDate()));
        }
        tvRating.setText(movie.getRating()+"");
        tvSynopsis.setText(movie.getSynopsis());
        Picasso.with(getActivity()).load(movie.getPosterUrl()).into(ivPoster);

        return rootView;
    }
}
