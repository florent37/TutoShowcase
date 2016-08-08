package com.github.florent37.tuto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Tuto.from(this)
            .setContentView(R.layout.tuto_sample)
            .addCircle(R.id.importantView, null)
            .addCircle(R.id.about, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            })
            .show();
    }
}
