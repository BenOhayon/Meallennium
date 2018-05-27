package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.utils.ProgressBarManager;

public class AddNewPostFragment extends Fragment {

    public interface AddNewPostFragmentListener {
        void onPost(Post post);
        void onCancel();
    }

    private AddNewPostFragmentListener listener;

    public AddNewPostFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_post, container, false);

        Button postButton = view.findViewById(R.id.createPostScreen__postButton);
        Button cancelButton = view.findViewById(R.id.createPostScreen__cancelButton);
        EditText postNameEt = view.findViewById(R.id.createPostScreen__postNameEt);
        TextInputEditText postDescEt = view.findViewById(R.id.createPostScreen__postDescTextArea);
        ProgressBar loadingProgressBar = view.findViewById(R.id.createPostScreen__progressBar);
        ProgressBarManager.bindProgressBar(loadingProgressBar);

        postButton.setOnClickListener((View v) -> {
            ProgressBarManager.showProgressBar();
            String name = postNameEt.getText().toString();
            String desc = postDescEt.getText().toString();
            Post newPost = new Post(name, desc);
            listener.onPost(newPost);
        });

        cancelButton.setOnClickListener((View v) -> {
            listener.onCancel();
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddNewPostFragmentListener) {
            listener = (AddNewPostFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
