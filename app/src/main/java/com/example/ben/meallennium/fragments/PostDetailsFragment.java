package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.dialogs.DeletePostConfirmDialog;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.ProgressBarManager;

import java.util.Objects;

public class PostDetailsFragment extends Fragment {

    public interface OnPostDetailsEventsListener {
        void onBack();
    }

    private ImageView postImage;
    private ProgressBar imageProgressBar;
    private Post post;

    private OnPostDetailsEventsListener listener;

    public PostDetailsFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnPostDetailsEventsListener) {
            listener = (OnPostDetailsEventsListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPostDetailsEventsListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        initializeViewElements(view);

        return view;
    }

    private void initializeViewElements(View view) {
        postImage = view.findViewById(R.id.postDetails__postImage);
        TextView postNameTv = view.findViewById(R.id.postDetails__postNameTv);
        TextView postDescTv = view.findViewById(R.id.postDetails__postDescTv);
        imageProgressBar = view.findViewById(R.id.postDetails__imageProgressBar);
        imageProgressBar.setVisibility(View.VISIBLE);
        ProgressBar centerProgressBar = view.findViewById(R.id.postDetails__centerProgressBar);
        ProgressBarManager.bindProgressBar(centerProgressBar);

        Bundle bundle = getArguments();
        post = (Post) Objects.requireNonNull(bundle).getSerializable("Post");
        postNameTv.setText(Objects.requireNonNull(post).getName());
        postDescTv.setText(post.getDescription());

        if (Model.getSignedInUser().equals(post.getPublisher())) {
            Button editButton = view.findViewById(R.id.postDetails__editButton);
            Button deleteButton = view.findViewById(R.id.postDetails__deleteButton);

            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);

            editButton.setOnClickListener((View v) -> {
                EditPostFragment editPostFragment = new EditPostFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("postToEdit", post);
                editPostFragment.setArguments(bundle1);
                FragmentTransactions.createAndDisplayFragment((AppCompatActivity) Objects.requireNonNull(getActivity()),
                        R.id.fragment_post_details_container, editPostFragment, true);
            });

            deleteButton.setOnClickListener((View v) -> {
                DeletePostConfirmDialog dialog = new DeletePostConfirmDialog();
                Bundle postToDeleteBundle = new Bundle();
                postToDeleteBundle.putSerializable("PostToDelete", post);
                dialog.setArguments(postToDeleteBundle);
                dialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "TAG");
            });
        } else {
            Button backButton = view.findViewById(R.id.postDetails__backButton);
            backButton.setVisibility(View.VISIBLE);

            backButton.setOnClickListener((View v) -> listener.onBack());
        }

        Model.instance.loadImage(post.getImageUrl(), (Bitmap pic) -> {
            if (pic != null) {
                postImage.setImageBitmap(pic);
            } else {
                postImage.setImageResource(R.drawable.about);
            }
            imageProgressBar.setVisibility(View.GONE);
        });
    }

}
