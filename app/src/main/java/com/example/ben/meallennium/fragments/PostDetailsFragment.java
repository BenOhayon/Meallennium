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
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.ProgressBarManager;

public class PostDetailsFragment extends Fragment {

    public interface OnDeleteButtonClicked {
        void onDelete();
    }

    private ImageView postImage;
    private TextView postNameTv;
    private TextView postDescTv;
    private Button editButton;
    private Button deleteButton;
    private ProgressBar imageProgressBar;
    private ProgressBar centerProgressBar;
    private Post post;

    private OnDeleteButtonClicked listener;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnDeleteButtonClicked) {
            listener = (OnDeleteButtonClicked)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDeleteButtonClicked");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        initializeViewElements(view);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPostFragment editPostFragment = new EditPostFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("postToEdit", post);
                editPostFragment.setArguments(bundle);
                FragmentTransactions.createAndDisplayFragment((AppCompatActivity)getActivity(), R.id.fragment_post_details_container, editPostFragment, true);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerProgressBar.setVisibility(View.VISIBLE);
                Model.instance.deletePost(post, new Model.OnOperationCompleteListener() {
                    @Override
                    public void onComplete() {
                        listener.onDelete();
                    }
                });
            }
        });

        return view;
    }

    private void initializeViewElements(View view) {
        postImage = view.findViewById(R.id.postDetails__postImage);
        postNameTv = view.findViewById(R.id.postDetails__postNameTv);
        postDescTv = view.findViewById(R.id.postDetails__postDescTv);
        editButton = view.findViewById(R.id.postDetails__editButton);
        deleteButton = view.findViewById(R.id.postDetails__deleteButton);
        imageProgressBar = view.findViewById(R.id.postDetails__imageProgressBar);
        imageProgressBar.setVisibility(View.VISIBLE);
        centerProgressBar = view.findViewById(R.id.postDetails__centerProgressBar);

        Bundle bundle = getArguments();
        post = (Post)bundle.getSerializable("Post");

        postNameTv.setText(post.getName());
        postDescTv.setText(post.getDescription());
        ProgressBarManager.showProgressBar();
        Model.instance.loadImage(post.getImageUrl(), new Model.OnFetchImageFromLocalCacheListener() {
            @Override
            public void onComplete(Bitmap pic) {
                postImage.setImageBitmap(pic);
                imageProgressBar.setVisibility(View.GONE);
            }
        });
    }

}
