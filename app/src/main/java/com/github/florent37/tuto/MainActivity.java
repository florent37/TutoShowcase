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

        findViewById(R.id.display).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTuto();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void displayTuto() {
        Tuto.from(this)
                .setContentView(R.layout.tuto_sample)

                .on(R.id.about)
                .addCircle(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })

                .on(R.id.swipable)
                .displaySwipableRight()
                .animated(true)

                .on(R.id.importantView)
                .addRoundRect(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })

                .show();
    }
}
