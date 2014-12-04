package com.hp.samples.moviesearch;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;

public class MovieDetailActivity extends Activity {

    public static final String TITLE = "TITLE";
    private ImageView posterView;
    private TextView titleView;
    private ProgressBar progress;
    private TextView countryView;
    private TextView ratingView;
    private TextView yearView;
    private TextView plotView;
    private View content;
    private Button addToWatchList;

    private Movie currentMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String title = getIntent().getStringExtra(TITLE);
        GetMovieDetailTask getMovieDetailTask = new GetMovieDetailTask();
        getMovieDetailTask.execute(title);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(title);
        getActionBar().setIcon(null);

        this.content = findViewById(R.id.content);

        this.posterView = (ImageView) findViewById(R.id.poster);
        this.titleView = (TextView) findViewById(android.R.id.title);
        this.plotView = (TextView) findViewById(R.id.plot);
        this.yearView = (TextView) findViewById(R.id.year);
        this.countryView = (TextView) findViewById(R.id.country);
        this.ratingView = (TextView) findViewById(R.id.rating);
        this.progress = (ProgressBar) findViewById(android.R.id.progress);
        this.addToWatchList = (Button) findViewById(R.id.add_to_watch_list);

        initializeWatchListButtonText(title);

        this.addToWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie currentMovie = MovieDetailActivity.this.currentMovie;
                if (currentMovie != null) {
                    Button clickedButton = (Button) v;
                    if (clickedButton.getText() == getResources().getString(R.string.remove_from_watchlist)) {
                        //currentMovie.delete();
                        Toast.makeText(MovieDetailActivity.this, MovieDetailActivity.this.currentMovie.getTitle() + " removed from watchlist", Toast.LENGTH_LONG).show();
                        toggleWatchListButton();
                    } else {
                        currentMovie.save();
                        Toast.makeText(MovieDetailActivity.this, MovieDetailActivity.this.currentMovie.getTitle() + " added to watchlist", Toast.LENGTH_LONG).show();
                        toggleWatchListButton();
                    }
                }
            }
        });
    }

    private void initializeWatchListButtonText(String title) {
        Movie movie = new Select().from(Movie.class).where("title = ?", title).executeSingle();
        if (movie != null) {
            addToWatchList.setText(R.string.remove_from_watchlist);
            MovieDetailActivity.this.currentMovie = movie;
        }
    }

    private void toggleWatchListButton() {
        if (this.addToWatchList.getText() == getResources().getString(R.string.remove_from_watchlist)) {
            this.addToWatchList.setText(R.string.add_to_watch_list);
        } else {
            this.addToWatchList.setText(R.string.remove_from_watchlist);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    protected class GetMovieDetailTask extends AsyncTask<String, Void, Movie> {

        public static final String IMDB_URL = "http://www.omdbapi.com/?t=%s";

        @Override
        protected Movie doInBackground(String... params) {
            String search = params[0];
            OkHttpClient client = new OkHttpClient();

            try {
                Request request = new Request.Builder()
                        .get()
                        .url(String.format(IMDB_URL, URLEncoder.encode(search, "UTF-8")))
                        .build();

                Response response = null;
                response = client.newCall(request).execute();
                String responseStr = response.body().string();
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setFieldNamingStrategy(new FieldNamingStrategy() {

                    private String capitalize(String line) {
                        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
                    }

                    @Override
                    public String translateName(Field f) {
                        if (f.getName().contains("imdbRating")) return f.getName();
                        return capitalize(f.getName());
                    }
                });

                Gson gson = gsonBuilder.create();

                Movie movie = gson.fromJson(responseStr, Movie.class);
                return movie;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            MovieDetailActivity.this.currentMovie = movie;
            super.onPostExecute(movie);
            Picasso.with(MovieDetailActivity.this).load(movie.getPoster()).into(posterView);
            titleView.setText(movie.getTitle());
            plotView.setText(movie.getPlot());
            yearView.setText(movie.getYear());
            countryView.setText(movie.getCountry());
            ratingView.setText(movie.getImdbRating());
            content.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }
}
