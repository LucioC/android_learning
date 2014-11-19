package com.hp.samples.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class RemindMeActivity extends Activity {

    private NotificationManager notificationManager;
    int id =0;

    private Handler handler;
    private EditText reminderField;
    private TimePicker timeToRemind;


    class RemindRunnable implements Runnable {

        String toRemind;

        RemindRunnable(String toRemind) {
            this.toRemind = toRemind;
        }

        @Override
        public void run() {
            Intent resultIntent = new Intent(getApplicationContext(), RemindMeActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addParentStack(RemindMeActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            final PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            Notification notification = new Notification.Builder(RemindMeActivity.this)
                    .setContentText(toRemind)
                    .setContentTitle("Reminder")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(resultPendingIntent)
                    .build();
            notificationManager.notify(id++, notification);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remindme);
        Button remindBtn = (Button) findViewById(R.id.remind_button);
        reminderField = (EditText) findViewById(R.id.reminder_field);
        timeToRemind = (TimePicker) findViewById(R.id.time_to_remind);
        timeToRemind.setIs24HourView(Boolean.TRUE);
        timeToRemind.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        handler = new Handler();
        remindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toRemind = reminderField.getText().toString();
                GregorianCalendar remindAt = new GregorianCalendar();

                remindAt.set(Calendar.HOUR_OF_DAY, timeToRemind.getCurrentHour());
                remindAt.set(Calendar.MINUTE, timeToRemind.getCurrentMinute());

                handler.postDelayed(new RemindRunnable(toRemind), remindAt.getTimeInMillis() - System.currentTimeMillis());

                reminderField.setText("");
                Toast.makeText(getApplicationContext(), "Reminder scheduled", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
