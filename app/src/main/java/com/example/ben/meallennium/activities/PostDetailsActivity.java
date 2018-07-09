package com.example.ben.meallennium.activities;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.utils.LogTag;
import com.example.ben.meallennium.utils.ProgressBarManager;

public class PostDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        ImageView postImage = findViewById(R.id.postDetails__postImage);
        TextView postNameTv = findViewById(R.id.postDetails__postNameTv);
        TextView postDescTv = findViewById(R.id.postDetails__postDescTv);
        Button editButton = findViewById(R.id.postDetails__editButton);
        Button deleteButton = findViewById(R.id.postDetails__deleteButton);
        ProgressBar progressBar = findViewById(R.id.postDetails__progressBar);
        ProgressBarManager.bindProgressBar(progressBar);


        Bundle bundle = getIntent().getExtras();
        Post post = (Post)bundle.getSerializable("Post");

        postNameTv.setText(post.getName());
        postDescTv.setText(post.getDescription());
        ProgressBarManager.showProgressBar();
        Model.instance.loadImage(post.getImageUrl(), new Model.OnFetchImageFromLocalCacheListener() {
            @Override
            public void onComplete(Bitmap pic) {
                postImage.setImageBitmap(pic);
                ProgressBarManager.dismissProgressBar();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBarManager.showProgressBar();
                Model.instance.deletePost(post, new Model.OnOperationCompleteListener() {
                    @Override
                    public void onComplete() {
                        finish();
                    }
                });
            }
        });
    }

    /*
        The below functionality is invoked whenever you click the back button
        in the action bar.
    */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
