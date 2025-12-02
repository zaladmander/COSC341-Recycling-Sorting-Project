package com.example.cosc341_recycling_sorting_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_SEEN_WELCOME = "seen_welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user has already seen this screen
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean seen = prefs.getBoolean(KEY_SEEN_WELCOME, false);

        if (seen) {
            // Skip straight to main content
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // First time: show welcome layout
        setContentView(R.layout.activity_welcome);

        findViewById(R.id.btnStart).setOnClickListener((View v) -> {
            // Mark welcome as seen
            prefs.edit().putBoolean(KEY_SEEN_WELCOME, true).apply();

            // Go to main activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
