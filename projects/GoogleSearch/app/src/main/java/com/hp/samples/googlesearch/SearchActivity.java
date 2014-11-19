package com.hp.samples.googlesearch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SearchActivity extends Activity {

    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/?#q=%s";
    private Button goBtn;
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        goBtn = (Button) findViewById(android.R.id.button1);
        searchField = (EditText) findViewById(android.R.id.edit);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = searchField.getText().toString();
                String searchUrl = String.format(GOOGLE_SEARCH_URL, subject);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("text/html");
                intent.setData(Uri.parse(searchUrl));
                startActivity(Intent.createChooser(intent, "Search"));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
