package com.example.ben.meallennium.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.activities.SearchResultsActivity;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.viewmodels.MyPostsListViewModel;
import com.example.ben.meallennium.utils.LogTag;

import java.util.List;
import java.util.Objects;

public class MyPostsListFragment extends Fragment {

    public interface MyPostsListFragmentListener {
        void onMyListItemClick(int clickedItemIndex);
    }

    private MyPostsListAdapter adapter;
    private PostsListFragment.OnSearchButtonClicked searchListener;

    class MyPostsListAdapter extends RecyclerView.Adapter<MyPostsListAdapter.PostViewHolder> {

        private MyPostsListFragmentListener listener;

        MyPostsListAdapter(MyPostsListFragmentListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_list_item,
                    parent, false);

            return new MyPostsListAdapter.PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return Objects.requireNonNull(Model.instance.getMyPostsData().getValue()).size();
        }


        public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView postItemName;
            ImageView postImage;
            ProgressBar imageProgressBar;

            private PostViewHolder(View itemView) {
                super(itemView);

                postItemName = itemView.findViewById(R.id.postItem_name);
                postImage = itemView.findViewById(R.id.postItem_image);
                imageProgressBar = itemView.findViewById(R.id.postItem_progressBar);
                itemView.setOnClickListener(this);
            }

            private void bind(int listIndex) {
                imageProgressBar.setVisibility(View.VISIBLE);
                Post post = Objects.requireNonNull(Model.instance.getMyPostsData().getValue()).get(listIndex);
                postItemName.setText(post.getName());

                if (post.getImageUrl() != null) {
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
                listener.onMyListItemClick(clickedPosition);
            }
        }
    }

    public MyPostsListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);

        RecyclerView postsList = view.findViewById(R.id.postsListScreen__list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        postsList.setLayoutManager(layoutManager);
        postsList.setHasFixedSize(true);
        adapter = new MyPostsListAdapter((MyPostsListFragmentListener) getActivity());
        postsList.setAdapter(adapter);
        Button searchButton = view.findViewById(R.id.postsListScreen__searchButton);
        EditText searchQuery = view.findViewById(R.id.postsListScreen__searchQuery);

        searchButton.setOnClickListener((View v) -> {
            if (!searchQuery.getText().toString().equals("")) {
                Intent toSearchResults = new Intent(getActivity(), SearchResultsActivity.class);
                Log.d(LogTag.TAG, "search query: " + searchQuery.getText().toString());
                toSearchResults.putExtra("query", searchQuery.getText().toString());
                toSearchResults.putExtra("list", 1);
                searchQuery.setText("");
                searchListener.onSearchButtonClick(toSearchResults);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof PostsListFragment.OnSearchButtonClicked) {
            searchListener = (PostsListFragment.OnSearchButtonClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchButtonClicked");
        }

        MyPostsListViewModel postsViewModel = ViewModelProviders.of(this).get(MyPostsListViewModel.class);
        postsViewModel.getMyPostsData().observe(this, (List<Post> posts) -> {
            adapter.notifyDataSetChanged();
            Log.d(LogTag.TAG, "LiveData has updated");
        });
    }
}
