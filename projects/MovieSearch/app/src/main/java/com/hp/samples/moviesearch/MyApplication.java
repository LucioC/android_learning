package com.hp.samples.moviesearch;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Configuration.Builder builder = new Configuration.Builder(this);
        builder.addModelClass(Movie.class);
        ActiveAndroid.initialize(builder.create());
    }
}