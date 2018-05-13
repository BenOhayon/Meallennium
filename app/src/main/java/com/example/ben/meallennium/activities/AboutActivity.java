package com.example.ben.meallennium.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ben.meallennium.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button OKButton = findViewById(R.id.aboutScreen__OKButton);
        OKButton.setOnClickListener((View view) -> {
            finish();
        });
    }
}
