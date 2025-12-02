package com.example.cosc341_recycling_sorting_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Button btnContinue = findViewById(R.id.btnContinueToMap);
        Button btnNoThanks = findViewById(R.id.btnNoThanks);
        TextView tvDataPolicy = findViewById(R.id.tvDataPolicy);

        btnContinue.setOnClickListener(v -> {
            Intent i = new Intent(PermissionActivity.this, MapActivity.class);
            startActivity(i);
        });
        btnNoThanks.setOnClickListener(v -> {
            Intent i = new Intent(PermissionActivity.this, ListActivity.class);
            startActivity(i);
        });
        tvDataPolicy.setOnClickListener(v -> {
            Intent i = new Intent(PermissionActivity.this, DataPolicyActivity.class);
            startActivity(i);
        });
    }
}
