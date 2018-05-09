package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.FloatingActionButton;

import com.example.ben.meallennium.R;

public class PostsListActivityFragment extends Fragment {

    public interface PostsListActivityFragmentListener {
        void onAddButtonPress();
        void onListItemSelect();
    }

    private PostsListActivityFragmentListener listener;

    public PostsListActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);

        RecyclerView list = view.findViewById(R.id.postsListScreen__list);
        list.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        FloatingActionButton fab = view.findViewById(R.id.postsListScreen__fab);
        fab.setOnClickListener((View v) -> {
            listener.onAddButtonPress();
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof PostsListActivityFragmentListener) {
            listener = (PostsListActivityFragmentListener) context;
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
}
