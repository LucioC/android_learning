package com.hp.samples.chronometer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class ChronometerActivity extends Activity {

    TextView clockTextView;
    Button startButton;
    Button pauseButton;
    Button stopButton;

    long time;

    Runnable incrementTask = new Runnable() {
        @Override
        public void run() {
            time += 1000;
            updateTime();
            handler.postDelayed(this, 1000);
        }
    };
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);
        handler = new Handler();
        clockTextView = (TextView) findViewById(R.id.time);
        startButton = (Button) findViewById(R.id.start);
        pauseButton = (Button) findViewById(R.id.pause);
        stopButton = (Button) findViewById(R.id.stop);

        time = 0;
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTime();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTime();
            }
        });
        updateTime();
    }

    private void updateTime() {
        String clockText = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(time)%60, TimeUnit.MILLISECONDS.toSeconds(time)%60);
        clockTextView.setText(clockText);
    }

    private void startTime() {
        handler.post(incrementTask);
        startButton.setClickable(Boolean.FALSE);
    }

    private void pauseTime() {
        handler.removeCallbacks(incrementTask);
        startButton.setClickable(Boolean.TRUE);
    }

    private void stopTime() {
        handler.removeCallbacks(incrementTask);
        time = 0;
        updateTime();
        startButton.setClickable(Boolean.TRUE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chronometer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
