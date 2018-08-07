package com.example.ben.meallennium.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.dialogs.DeletePostConfirmDialog;
import com.example.ben.meallennium.fragments.EditPostFragment;
import com.example.ben.meallennium.fragments.PostDetailsFragment;
import com.example.ben.meallennium.utils.FragmentTransactions;

public class PostDetailsActivity extends AppCompatActivity implements
        EditPostFragment.OnCancelButtonClicked,
        EditPostFragment.OnEditCompleteListener,
        PostDetailsFragment.OnPostDetailsEventsListener,
        DeletePostConfirmDialog.DeletePostConfirmDialogListener {

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onYesClickedOnDeletePostDialog() {
        finish();
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
    public void onBack() {
        finish();
    }
}
