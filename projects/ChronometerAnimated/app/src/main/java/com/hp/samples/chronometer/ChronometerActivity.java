package com.hp.samples.chronometer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
            animateClock();
            handler.postDelayed(this, 1000);
        }
    };
    private Handler handler;
    private float currentRotation = 0f;
    private ObjectAnimator objectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chronometer);
        handler = new Handler();
        clockTextView = (TextView) findViewById(R.id.time);
        startButton = (Button) findViewById(R.id.start);
        pauseButton = (Button) findViewById(R.id.pause);
        stopButton = (Button) findViewById(R.id.stop);

        pauseButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);

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
        String clockText = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(time) % 60, TimeUnit.MILLISECONDS.toSeconds(time) % 60);
        clockTextView.setText(clockText);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                objectAnimator = ObjectAnimator.ofFloat(clockTextView, View.ROTATION, currentRotation, currentRotation += 10.2f);
//                objectAnimator.setDuration(600);
//                objectAnimator.start();
//            }
//        });

    }

    private void startTime() {
        handler.post(incrementTask);
        startButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);

        animateClock();
    }

    private void animateClock() {
        ObjectAnimator yAnimator = ObjectAnimator.ofFloat(clockTextView, View.SCALE_Y, 1f, 1.5f, 1f);
        ObjectAnimator xAnimator = ObjectAnimator.ofFloat(clockTextView, View.SCALE_X, 1f, 1.5f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(clockTextView, View.ALPHA, 1f, 0.5f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(xAnimator, yAnimator, alpha);
        animatorSet.start();
    }

    private void pauseTime() {
        handler.removeCallbacks(incrementTask);
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);
    }

    private void stopTime() {
        handler.removeCallbacks(incrementTask);
        time = 0;
        updateTime();
        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.GONE);
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
