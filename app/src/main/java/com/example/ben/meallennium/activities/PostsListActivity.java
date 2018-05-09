package com.example.ben.meallennium.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.fragments.AddNewPostFragment;
import com.example.ben.meallennium.fragments.PostsListActivityFragment;
import com.example.ben.meallennium.utils.FragmentTransactions;

public class PostsListActivity extends AppCompatActivity implements
        PostsListActivityFragment.PostsListActivityFragmentListener,
        AddNewPostFragment.AddNewPostFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        PostsListActivityFragment postsListActivityFragment = new PostsListActivityFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_posts_list_container, postsListActivityFragment, false);
    }

    @Override
    public void onAddButtonPress() {
        AddNewPostFragment addNewPostFragment = new AddNewPostFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_posts_list_container,
                addNewPostFragment, true);
    }

    @Override
    public void onListItemSelect() {

    }

    @Override
    public void onPost() {
        // TODO add the logic of saving the post details locally and in Firebase in time.
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCancel() {
        getSupportFragmentManager().popBackStack();
    }

}
