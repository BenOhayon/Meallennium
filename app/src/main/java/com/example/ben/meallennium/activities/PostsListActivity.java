package com.example.ben.meallennium.activities;

import android.app.SearchManager;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
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
import android.support.v7.widget.SearchView;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.dialogs.DeleteAccountConfirmationDialog;
import com.example.ben.meallennium.dialogs.DeletePostConfirmDialog;
import com.example.ben.meallennium.dialogs.LogoutConfirmationDialog;
import com.example.ben.meallennium.fragments.MyPostsListFragment;
import com.example.ben.meallennium.fragments.PostsListFragment;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.model.sql.PostAsyncDao;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.LogTag;
import com.example.ben.meallennium.utils.ProgressBarManager;
import com.example.ben.meallennium.utils.Requests;
import com.example.ben.meallennium.utils.Results;

import java.util.LinkedList;
import java.util.List;

public class PostsListActivity extends AppCompatActivity implements
        PostsListFragment.PostsListFragmentListener,
        MyPostsListFragment.MyPostsListFragmentListener,
        DeleteAccountConfirmationDialog.DeleteAccountConfirmationDialogListener,
        LogoutConfirmationDialog.LogoutConfirmationDialogListener {

    public static String SIGNED_IN_USERNAME;
    private ViewPager viewPager;
    private List<Post> originalAllPosts, originalMyPosts; // for searching

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
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

        SIGNED_IN_USERNAME = getSharedPreferences("SP", MODE_PRIVATE).getString("userName", "default name");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Welcome, " + SIGNED_IN_USERNAME);
        }

//        TextView startMessage = findViewById(R.id.postsListScreen__startMessage);
//        if(Model.instance.getPostsData().getValue().size() == 0) {
//
//        }

        FloatingActionButton addFab = findViewById(R.id.postsListScreen__addFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToAddNewPostActivity();
            }
        });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.fragment_posts_list_container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

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
                Post post = new Post(SIGNED_IN_USERNAME, postName, postDesc);

                if(imageUrl != null) {
                    post.setImageUrl(imageUrl);
                }

                Model.instance.addPost(SIGNED_IN_USERNAME,
                        post, new FirebaseModel.OnCreateNewPostListener() {
                            @Override
                            public void onComplete(Post post) {
                                ProgressBarManager.dismissProgressBar();
                            }
                        }, new PostAsyncDao.PostAsyncDaoListener<Boolean>() {
                            @Override
                            public void onComplete(Boolean result) {
                                Log.d(LogTag.TAG, "New post " + post.getId() + " was added to local DB.");
                            }
                        });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_posts_list, menu);

//        MenuItem menuItem = menu.findItem(R.id.actionMenu__search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setOnQueryTextListener(this);
//        searchView.setQueryHint("Type a dish or restaurant");
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                Model.instance.getAllPostsFromLocalDB(new FirebaseModel.OnFetchAllPostsListener() {
//                    @Override
//                    public void onComplete(List<Post> posts) {
//                        ((MutableLiveData<List<Post>>) Model.instance.getPostsData()).setValue(posts);
//                    }
//                });
//                return true;
//            }
//        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.actionMenu__search).getActionView();
        searchView.setQueryHint("Type a dish or restaurant");
        if(searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                MutableLiveData<List<Post>> allPostsLiveData = (MutableLiveData<List<Post>>) Model.instance.getPostsData();
                MutableLiveData<List<Post>> myPostsLiveData = (MutableLiveData<List<Post>>) Model.instance.getMyPostsData();

                allPostsLiveData.setValue(originalAllPosts);
                myPostsLiveData.setValue(originalMyPosts);
                return true;
            }
        });

        return true;
    }

    // --------------------
    //   LISTENER METHODS
    // --------------------


    /*
     * For the use of PostListAdapter
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Post selectedPost = Model.instance.getPostsData().getValue().get(clickedItemIndex);
        moveToPostDetailsActivity(selectedPost);
    }

    @Override
    public void onMyListItemClick(int clickedItemIndex) {
        Post selectedPost = Model.instance.getMyPostsData().getValue().get(clickedItemIndex);
        moveToPostDetailsActivity(selectedPost);
    }

    @Override
    public void onAddFabClick() {
        moveToAddNewPostActivity();
    }

    @Override
    public void onYesClickedOnDeleteAccountDialog() {
        finish();
    }

    @Override
    public void onYesClickedOnLogoutDialog() {
        finish();
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
            String userInput = query.toLowerCase();

//            MutableLiveData<List<Post>> allPostsLiveData = (MutableLiveData<List<Post>>) Model.instance.getPostsData();
//            MutableLiveData<List<Post>> myPostsLiveData = (MutableLiveData<List<Post>>) Model.instance.getMyPostsData();
//
//            originalAllPosts = allPostsLiveData.getValue();
//            originalMyPosts = myPostsLiveData.getValue();
//
//            List<Post> newAllPosts = new LinkedList<>();
//            List<Post> newMyPosts = new LinkedList<>();
//
//            for(Post post : originalAllPosts) {
//                if(post.getName().contains(userInput) || post.getPublisher().contains(userInput)) {
//                    newAllPosts.add(post);
//                }
//            }
//
//            for(Post post : originalMyPosts) {
//                if(post.getName().contains(userInput) || post.getPublisher().contains(userInput)) {
//                    newMyPosts.add(post);
//                }
//            }
//
//            allPostsLiveData.setValue(newAllPosts);
//            myPostsLiveData.setValue(newMyPosts);
        }
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
