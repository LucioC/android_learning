package com.hp.samples.moviesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.List;


public class SearchResultActivity extends Activity {

    public static final String TO_SEARCH = "TO_SEARCH";
    ListView listView;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String toSearch = getIntent().getStringExtra(TO_SEARCH);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(String.format("Searching for: %s", toSearch));
        getActionBar().setIcon(null);

        listView = (ListView) findViewById(android.R.id.list);
        spinner = (ProgressBar) findViewById(android.R.id.progress);
        SearchMoviesTask searchMoviesTask = new SearchMoviesTask();
        searchMoviesTask.execute(toSearch);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    public class SearchMoviesTask extends AsyncTask<String, Void, SearchResult> {

        public static final String IMDB_URL = "http://www.omdbapi.com/?s=%s";

        @Override
        protected SearchResult doInBackground(String... params) {
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
                        return capitalize(f.getName());
                    }
                });

                Gson gson = gsonBuilder.create();


                SearchResult searchResult = gson.fromJson(responseStr, SearchResult.class);
                return searchResult;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Request request = null;
            return null;

        }

        @Override
        protected void onPostExecute(final SearchResult searchResult) {
            super.onPostExecute(searchResult);

            if(searchResult.getError() != null && !searchResult.getError().isEmpty()) {
                Toast.makeText(SearchResultActivity.this, searchResult.getError(), Toast.LENGTH_LONG).show();
                finish();
            } else {
                displayContent(searchResult);
            }

        }

        private void displayContent(SearchResult searchResult) {
            spinner.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            final List<Movie> movies = searchResult.getSearch();
            listView.setAdapter(new MovieListAdapter(SearchResultActivity.this, movies));
            prepareDetailOnItemClick(movies);
        }

        private void prepareDetailOnItemClick(final List<Movie> movies) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = movies.get(position);
                    Intent intent = new Intent(SearchResultActivity.this, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailActivity.TITLE, movie.getTitle());
                    startActivity(intent);
                }
            });
        }
    }
}
