package com.example.ben.meallennium.activities;

import android.content.Intent;
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
import com.example.ben.meallennium.fragments.EditPostFragment;
import com.example.ben.meallennium.fragments.PostDetailsFragment;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.LogTag;
import com.example.ben.meallennium.utils.ProgressBarManager;

public class PostDetailsActivity extends AppCompatActivity implements
        EditPostFragment.OnCancelButtonClicked,
        EditPostFragment.OnEditCompleteListener,
        PostDetailsFragment.OnPostDetailsEventsListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent fromPostListActivity = getIntent();
        PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
        postDetailsFragment.setArguments(fromPostListActivity.getExtras());
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_post_details_container, postDetailsFragment, false);
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

    @Override
    public void onEditDone() {
        finish();
    }

    @Override
    public void onCancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDelete() {
        finish();
    }

    @Override
    public void onBack() {
        finish();
    }
}
