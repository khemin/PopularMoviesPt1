package com.khemin.and.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Khemin on 2/8/15.
 */
public class MoviesAdapter extends BaseAdapter {
    Context mContext;
    List<Movie> data;
    public MoviesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            View view = View.inflate(mContext, R.layout.grid_item_poster, null);
            imageView = (ImageView) view.findViewById(R.id.iv_poster);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(data.get(position).getPosterUrl()).into(imageView);
        return imageView;
    }

    public void setData(List<Movie> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }
}
