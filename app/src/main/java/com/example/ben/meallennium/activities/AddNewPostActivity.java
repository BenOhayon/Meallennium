package com.example.ben.meallennium.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ben.meallennium.R;

public class AddNewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);

        Button postButton = findViewById(R.id.createPostScreen__postButton);
        Button cancelButton = findViewById(R.id.createPostScreen__cancelButton);

        postButton.setOnClickListener((View view) -> {
            // TODO insert the post creation logic here.
            Toast message = Toast.makeText(this, "A post was created!", Toast.LENGTH_LONG);
            message.show();
            finish();
        });

        cancelButton.setOnClickListener((View view) -> {
            finish();
        });
    }
}
