package com.example.ben.meallennium.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.adapters.PostsListAdapter;
import com.example.ben.meallennium.dialogs.DeleteAccountConfirmationDialog;
import com.example.ben.meallennium.dialogs.LogoutConfirmationDialog;
import com.example.ben.meallennium.fragments.AboutFragment;
import com.example.ben.meallennium.fragments.AddNewPostFragment;
import com.example.ben.meallennium.fragments.PostsListFragment;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.ToastMessageDisplayer;

public class PostsListActivity extends AppCompatActivity implements
        PostsListFragment.PostsListFragmentListener,
        AddNewPostFragment.AddNewPostFragmentListener,
        AboutFragment.AboutFragmentListener,
        FirebaseModel.FirebaseUserDataListener {

    private PostsListAdapter adapter;

    // --------------------
    //   CALLBACK METHODS
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        Model.instnace.setListenerForFirebaseUserData(this);
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

            case R.id.actionMenu__add:
                Intent toCreatePostScreen = new Intent(this, AddNewPostActivity.class);
                startActivity(toCreatePostScreen);
                break;

            case R.id.actionMenu__deleteAccount:
                new DeleteAccountConfirmationDialog().show(getSupportFragmentManager(), "TAG");
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_posts_list, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.actionMenu__search).getActionView();
        searchView.setQueryHint("Type a dish or restaurant");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    // --------------------
    //   LISTENER METHODS
    // --------------------

    @Override
    public void onListItemSelect(int clickedItemIndex) {
        Model.instnace.popUpAllUsers();
    }

    @Override
    public void onPost() {
        // TODO add the logic of saving the post details locally and in Firebase.
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onOKPressed() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDeleteUser(User user) {
        Model.instnace.setSignedInUserInFirebase(null);
        ToastMessageDisplayer.displayToast(this, "The user was deleted.");
        finish();
    }

    // ----------------------------
    //   PRIVATE & PUBLIC METHODS
    // ----------------------------

    /**
     * Handles the action search intent that starts this activity whenever we wish
     * to perform a search task in the activity.
     * @param intent The intent to handle.
     */
    private void handleIntent(Intent intent) {

        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // TODO enter the search logic here.

        }
    }

    public void setAdapter(PostsListAdapter adapter) {
        this.adapter = adapter;
    }
}
