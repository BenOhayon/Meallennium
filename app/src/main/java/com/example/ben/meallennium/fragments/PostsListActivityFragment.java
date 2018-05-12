package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.adapters.PostsListAdapter;
import com.example.ben.meallennium.model.Model;

public class PostsListActivityFragment extends Fragment implements PostsListAdapter.ListItemClickListener {

    private static final int NUM_LIST_ITEMS = 20;
    private PostsListAdapter adapter;
    private RecyclerView postsList;

    public interface PostsListFragmentListener {
        void onAddButtonPress();
        void onListItemSelect();
    }

    private PostsListFragmentListener listener;

    public PostsListActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);

        postsList = view.findViewById(R.id.postsListScreen__list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        postsList.setLayoutManager(layoutManager);
        postsList.setHasFixedSize(true);
        adapter = new PostsListAdapter(NUM_LIST_ITEMS, this);
        postsList.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.postsListScreen__fab);
        fab.setOnClickListener((View v) -> {
            listener.onAddButtonPress();
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof PostsListFragmentListener) {
            listener = (PostsListFragmentListener ) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PostsListActivityFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        listener.onListItemSelect();
    }
}
