package com.example.ben.meallennium.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.dialogs.DeleteAccountConfirmationDialog;
import com.example.ben.meallennium.dialogs.LogoutConfirmationDialog;
import com.example.ben.meallennium.fragments.PostsListFragment;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.ProgressBarManager;
import com.example.ben.meallennium.utils.Requests;
import com.example.ben.meallennium.utils.Results;

import java.util.LinkedList;
import java.util.List;

public class PostsListActivity extends AppCompatActivity implements
        PostsListFragment.PostsListFragmentListener {

    // --------------------
    //   CALLBACK METHODS
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Welcome, " + getSharedPreferences("SP", MODE_PRIVATE).getString("userName", "default name"));
        }

        handleIntent(getIntent());

        PostsListFragment postsListFragment = new PostsListFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_posts_list_container, postsListFragment, false);
    }

    @Override
    public void onNewIntent(Intent intent) {
        handleIntent(intent);
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

            case R.id.actionMenu__deleteAccount:
                new DeleteAccountConfirmationDialog().show(getSupportFragmentManager(), "TAG");
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
                Post post = new Post(postName, postDesc);

                if(imageUrl != null) {
                    post.setImageUrl(imageUrl);
                }

                Model.instance.addPostToFirebase(post, new FirebaseModel.OnCreateNewPostListener() {
                    @Override
                    public void onComplete(Post post) {
                        Model.instance.addPostToLocalDB(post, null);
                        ProgressBarManager.dismissProgressBar();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_posts_list, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.actionMenu__search).getActionView();
        searchView.setQueryHint("Type a dish or restaurant");
        if(searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        return true;
    }

    // --------------------
    //   LISTENER METHODS
    // --------------------

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Post selectedPost = Model.instance.getPostsData().getValue().get(clickedItemIndex);

        Intent toPostDetailsActivity = new Intent(this, PostDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", selectedPost);
        toPostDetailsActivity.putExtras(bundle);
        startActivity(toPostDetailsActivity);
    }

    @Override
    public void onAddFabClick() {
        moveToAddNewPostActivity();
    }

    // ----------------------------
    //   PRIVATE & PUBLIC METHODS
    // ----------------------------

    /**
     * Handles the action search intent that starts this activity whenever we wish
     * to perform a search task in the activity.
     *
     * @param intent The intent to handle.
     */
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // TODO enter the search logic here.

            List<Post> postsToFilter = new LinkedList<>();
            for(Post post : Model.instance.getPostsData().getValue()) {
                if(post.getName().contains(query)) {
                    postsToFilter.add(post);
                    Log.d("buildTest", "Post name: " + post.getName() + ", Post Description: " + post.getDescription());
                }
            }
        }
    }

    private void moveToAddNewPostActivity() {
        Intent toCreatePostScreen = new Intent(this, AddNewPostActivity.class);
        startActivityForResult(toCreatePostScreen, Requests.NEW_POST_REQUEST);
    }
}
