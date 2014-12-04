package com.hp.samples.moviesearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

public class MovieListAdapter extends ArrayAdapter<Movie> {

    private final Context context;
    private final Vector<Movie> movies;
    private final LayoutInflater inflater;

    public MovieListAdapter(Context context, List<Movie> objects) {
        super(context, R.layout.movie_row, objects);
        this.context = context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.movies = new Vector<Movie>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.movie_row, parent, false);
        TextView title = (TextView) rowView.findViewById(android.R.id.text1);
        title.setText(movies.get(position).getTitle());

        return rowView;
    }
}
