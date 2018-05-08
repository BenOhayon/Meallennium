package com.example.ben.meallennium.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.fragments.PostsListActivityFragment;
import com.example.ben.meallennium.utils.FragmentTransactions;

public class PostsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        PostsListActivityFragment postsListActivityFragment = new PostsListActivityFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_posts_list_container, postsListActivityFragment, false);
    }

}
