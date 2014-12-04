package com.hp.samples.moviesearch;

import java.util.List;

public class SearchResult {

    List<Movie> search;

    String error;

    public List<Movie> getSearch() {
        return search;
    }

    public void setSearch(List<Movie> search) {
        this.search = search;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}

