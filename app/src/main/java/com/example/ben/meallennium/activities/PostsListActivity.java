package com.example.ben.meallennium.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.dialogs.DeleteAccountConfirmationDialog;
import com.example.ben.meallennium.dialogs.LogoutConfirmationDialog;
import com.example.ben.meallennium.fragments.MyPostsListFragment;
import com.example.ben.meallennium.fragments.PostsListFragment;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.LogTag;
import com.example.ben.meallennium.utils.ProgressBarManager;
import com.example.ben.meallennium.utils.Requests;
import com.example.ben.meallennium.utils.Results;

import java.util.Objects;

public class PostsListActivity extends AppCompatActivity implements
        PostsListFragment.PostsListFragmentListener,
        MyPostsListFragment.MyPostsListFragmentListener,
        DeleteAccountConfirmationDialog.DeleteAccountConfirmationDialogListener,
        LogoutConfirmationDialog.LogoutConfirmationDialogListener,
        PostsListFragment.OnSearchButtonClicked {

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new PostsListFragment();
            }

            return new MyPostsListFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        Model.setSignedInUser(getSharedPreferences("SP", MODE_PRIVATE).getString("userName", "default name"));
        Log.d(LogTag.TAG, getSharedPreferences("SP", MODE_PRIVATE).getString("userName", "default name") + " has signed in");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Welcome, " + Model.getSignedInUser());
        }

        FloatingActionButton addFab = findViewById(R.id.postsListScreen__addFab);
        addFab.setOnClickListener((View v) -> moveToAddNewPostActivity());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.fragment_posts_list_container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        PostsListFragment postsListFragment = new PostsListFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_posts_list_container, postsListFragment, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionMenu__logout:
                new LogoutConfirmationDialog().show(getSupportFragmentManager(), "TAG");
                break;

            case R.id.actionMenu__about:
                Intent toAboutScreen = new Intent(this, AboutActivity.class);
                startActivity(toAboutScreen);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Requests.NEW_POST_REQUEST) {
            if(resultCode == Results.POST_CREATION_SUCCESS) {
                ProgressBarManager.showProgressBar();
                String postName = data.getStringExtra("postName");
                String postDesc = data.getStringExtra("postDesc");
                String imageUrl = data.getStringExtra("imageURL");
                Post post = new Post(Model.getSignedInUser(), postName, postDesc);

                if(imageUrl != null) {
                    post.setImageUrl(imageUrl);
                }

                Model.instance.addPost(Model.getSignedInUser(),
                        post, (Post post1) -> ProgressBarManager.dismissProgressBar(),
                        (Boolean result) -> Log.d(LogTag.TAG, "New post " + post.getId() + " was added to local DB."));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_posts_list, menu);
        return true;
    }

    /*
     * For the use of PostListAdapter
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Post selectedPost = Objects.requireNonNull(Model.instance.getPostsData().getValue()).get(clickedItemIndex);
        moveToPostDetailsActivity(selectedPost);
    }

    @Override
    public void onMyListItemClick(int clickedItemIndex) {
        Post selectedPost = Objects.requireNonNull(Model.instance.getMyPostsData().getValue()).get(clickedItemIndex);
        moveToPostDetailsActivity(selectedPost);
    }

    @Override
    public void onSearchButtonClick(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onYesClickedOnDeleteAccountDialog() {
        finish();
    }

    @Override
    public void onYesClickedOnLogoutDialog() {
        finish();
    }

    private void moveToPostDetailsActivity(Post selectedPost) {
        Intent toPostDetailsActivity = new Intent(this, PostDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", selectedPost);
        toPostDetailsActivity.putExtras(bundle);
        startActivity(toPostDetailsActivity);
    }

    private void moveToAddNewPostActivity() {
        Intent toCreatePostScreen = new Intent(this, AddNewPostActivity.class);
        startActivityForResult(toCreatePostScreen, Requests.NEW_POST_REQUEST);
    }
}
