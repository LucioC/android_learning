package com.hp.samples.clock;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ClockActivity extends Activity {

    TextView clock = null;
    Runnable runnable;
    Handler handler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        clock = (TextView) findViewById(R.id.clock);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String dateFormatted = formatter.format(date);
                clock.setText(dateFormatted);

                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                handler.postAtTime(runnable, next);
            }
        };
        handler.post(runnable);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.clock, menu);
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
