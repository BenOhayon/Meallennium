package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.adapters.PostsListAdapter;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.utils.ProgressBarManager;

import java.util.List;

public class PostsListFragment extends Fragment implements
        PostsListAdapter.ListItemClickListener,
        FirebaseModel.FirebaseDataManagerListener {

    private PostsListAdapter adapter;

    public interface PostsListFragmentListener {
        void onListItemSelect(int clickedItemIndex);
    }

    private PostsListFragmentListener listener;

    public PostsListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model.instance.setListenerForFirebaseDataManager(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);

        ProgressBar loadingProgressBar = view.findViewById(R.id.postsListScreen__progressBar);
        ProgressBarManager.bindProgressBar(loadingProgressBar);
        ProgressBarManager.showProgressBar();

        // TODO Solve the bug of displaying 2 copies of a created post.
        if(savedInstanceState == null) {
            Log.d("buildTest", "savedInstanceState is null!");
            Model.instance.fetchAllPostsDataFromFirebase();
        }

        RecyclerView postsList = view.findViewById(R.id.postsListScreen__list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        postsList.setLayoutManager(layoutManager);
        postsList.setHasFixedSize(true);
        adapter = new PostsListAdapter(this);
        postsList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof PostsListFragmentListener) {
            listener = (PostsListFragmentListener ) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PostsListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        listener.onListItemSelect(clickedItemIndex);
    }

    @Override
    public void onFetchAllPosts(List<Post> posts) {
        Model.instance.setPostsData(posts);
//        adapter.setPostsToDisplay(Model.instance.getPostsData());
        adapter.notifyDataSetChanged();

        Bundle args = new Bundle();
        setArguments(args);
        Log.d("buildTest", "setArguments(args) was called");

        ProgressBarManager.dismissProgressBar();
    }

    @Override
    public void onCreateNewPost(Post post) {
        Model.instance.getPostsData().add(post);
//        adapter.setPostsToDisplay(Model.instance.getPostsData());
        adapter.notifyDataSetChanged();
        ProgressBarManager.dismissProgressBar();
    }
}
