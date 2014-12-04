package com.hp.samples.moviesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

public class WatchlistActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Watchlist");

        this.listView = (ListView) findViewById(android.R.id.list);
        List<Movie> movies = new Select().all().from(Movie.class).execute();
        listView.setAdapter(new MovieListAdapter(this, movies));
        prepareDetailOnItemClick(movies);
    }

    private void prepareDetailOnItemClick(final List<Movie> movies) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movies.get(position);
                Intent intent = new Intent(WatchlistActivity.this, MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.TITLE, movie.getTitle());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
