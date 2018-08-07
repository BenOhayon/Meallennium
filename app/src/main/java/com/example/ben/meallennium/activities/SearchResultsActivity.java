package com.example.ben.meallennium.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.fragments.PostsListFragment;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.sql.PostAsyncDao;
import com.example.ben.meallennium.utils.LogTag;
import com.example.ben.meallennium.utils.ToastMessageDisplayer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements PostsListFragment.PostsListFragmentListener {

    private List<Post> searchResults = new LinkedList<>();
    private SearchResultsPostsListAdapter adapter;

    class SearchResultsPostsListAdapter extends RecyclerView.Adapter<SearchResultsPostsListAdapter.SearchResultsPostViewHolder> {

        private PostsListFragment.PostsListFragmentListener listener;

        SearchResultsPostsListAdapter(PostsListFragment.PostsListFragmentListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public SearchResultsPostsListAdapter.SearchResultsPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_list_item,
                    parent, false);
            return new SearchResultsPostsListAdapter.SearchResultsPostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchResultsPostsListAdapter.SearchResultsPostViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return searchResults.size();
        }


        public class SearchResultsPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView postItemName, publisherName;
            ImageView postImage;
            ProgressBar imageProgressBar;

            private SearchResultsPostViewHolder(View itemView) {
                super(itemView);

                postItemName = itemView.findViewById(R.id.postItem_name);
                postImage = itemView.findViewById(R.id.postItem_image);
                publisherName = itemView.findViewById(R.id.postItem_publisherName);
                imageProgressBar = itemView.findViewById(R.id.postItem_progressBar);
                itemView.setOnClickListener(this);
            }

            private void bind(int listIndex) {
                imageProgressBar.setVisibility(View.VISIBLE);
                Post post = searchResults.get(listIndex);
                postItemName.setText(post.getName());
                publisherName.setText(getResources().getString(R.string.PostByPublisherName, Model.getSignedInUser()));

                if(post.getImageUrl() != null) {
                    Model.instance.loadImage(post.getImageUrl(), (Bitmap pic) -> {
                        Log.d(LogTag.TAG, "Retrieving picture from local cache");
                        if (pic != null) {
                            postImage.setImageBitmap(pic);
                        } else {
                            postImage.setImageResource(R.drawable.about);
                        }
                        imageProgressBar.setVisibility(View.GONE);
                    });
                } else {
                    postImage.setImageResource(R.drawable.about);
                    imageProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onClick(View v) {
                int clickedPosition = getAdapterPosition();
                listener.onListItemClick(clickedPosition);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent fromPostsList = getIntent();
        String searchQuery = fromPostsList.getStringExtra("query");
        int requestingList = fromPostsList.getIntExtra("list", -1);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Results for '" + searchQuery + "'");
        }

        RecyclerView searchResultsPostList = findViewById(R.id.searchResultsListScreen__list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchResultsPostList.setLayoutManager(layoutManager);
        searchResultsPostList.setHasFixedSize(true);
        adapter = new SearchResultsPostsListAdapter(this);
        searchResultsPostList.setAdapter(adapter);

        switch(requestingList) {
            case 0:
                PostAsyncDao.getAllPosts((List<Post> result) -> {
                    Iterator iter = result.iterator();
                    while(iter.hasNext()) {
                        Post p = (Post)iter.next();
                        if(!p.getPublisher().startsWith(searchQuery) && !p.getName().startsWith(searchQuery)) {
                            iter.remove();
                        }
                    }

                    searchResults.addAll(result);
                    adapter.notifyDataSetChanged();
                });
                break;

            case 1:
                PostAsyncDao.getPostsByPublisher(Model.getSignedInUser(), (List<Post> result) -> {
                    Iterator iter = result.iterator();
                    while(iter.hasNext()) {
                        Post p = (Post)iter.next();
                        if(!p.getName().startsWith(searchQuery)) {
                            iter.remove();
                        }
                    }

                    searchResults.addAll(result);
                    adapter.notifyDataSetChanged();
                });
                break;

            default:
                ToastMessageDisplayer.displayToast(this, "Something went wrong with requester list");
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Post post = searchResults.get(clickedItemIndex);
        moveToPostDetailsActivity(post);
    }

    private void moveToPostDetailsActivity(Post selectedPost) {
        Intent toPostDetailsActivity = new Intent(this, PostDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post", selectedPost);
        toPostDetailsActivity.putExtras(bundle);
        startActivity(toPostDetailsActivity);
    }
}
