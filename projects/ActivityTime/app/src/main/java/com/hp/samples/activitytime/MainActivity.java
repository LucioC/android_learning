package com.hp.samples.activitytime;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {

    private TextView timer = null;
    private Runnable runnable;
    private Handler handler;
    private long lastTime;
    private long totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = (TextView) findViewById(R.id.timer);
        totalTime = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        lastTime = SystemClock.uptimeMillis();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                long time = SystemClock.uptimeMillis();
                totalTime += time - lastTime;
                lastTime = time;

                updateTextTime(totalTime);

                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                handler.postAtTime(runnable, next);
            }
        };
        handler.post(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void updateTextTime(long time) {
        timer.setText("elapsed seconds: " + TimeUnit.MILLISECONDS.toSeconds(time));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        long time = SystemClock.uptimeMillis();
        totalTime += time - lastTime;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
