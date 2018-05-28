package com.example.ben.meallennium.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.utils.ProgressBarManager;
import com.example.ben.meallennium.utils.Results;

import java.util.List;

public class AddNewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);

        Button postButton = findViewById(R.id.createPostScreen__postButton);
        Button cancelButton = findViewById(R.id.createPostScreen__cancelButton);
        EditText postNameEt = findViewById(R.id.createPostScreen__postNameEt);
        TextInputEditText postDescEt = findViewById(R.id.createPostScreen__postDescTextArea);
        ProgressBar loadingProgressBar = findViewById(R.id.createPostScreen__progressBar);
        ProgressBarManager.bindProgressBar(loadingProgressBar);

        postButton.setOnClickListener((View v) -> {
            ProgressBarManager.showProgressBar();
            String name = postNameEt.getText().toString();
            String desc = postDescEt.getText().toString();

            Intent newPostIntent = getIntent();
            newPostIntent.putExtra("postName", name);
            newPostIntent.putExtra("postDesc", desc);
            setResult(Results.POST_CREATION_SUCCESS, newPostIntent);
            finish();
        });

        cancelButton.setOnClickListener((View v) -> {
            finish();
        });
    }
}
