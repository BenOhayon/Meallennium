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
import com.example.ben.meallennium.activities.PostsListActivity;
import com.example.ben.meallennium.dialogs.DeletePostConfirmDialog;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.ProgressBarManager;

public class PostDetailsFragment extends Fragment {

//    public interface OnDeleteButtonClicked {
//        void onDelete();
//    }
//
//    public interface OnBackButtonClicked {
//        void onBack();
//    }

    public interface OnPostDetailsEventsListener {
        void onDelete();
        void onBack();
    }

    private ImageView postImage;
    private TextView postNameTv;
    private TextView postDescTv;
    private Button editButton, deleteButton, backButton;
    private ProgressBar imageProgressBar;
    private ProgressBar centerProgressBar;
    private Post post;

    private OnPostDetailsEventsListener listener;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

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
        postNameTv = view.findViewById(R.id.postDetails__postNameTv);
        postDescTv = view.findViewById(R.id.postDetails__postDescTv);
        imageProgressBar = view.findViewById(R.id.postDetails__imageProgressBar);
        imageProgressBar.setVisibility(View.VISIBLE);
        centerProgressBar = view.findViewById(R.id.postDetails__centerProgressBar);
        ProgressBarManager.bindProgressBar(centerProgressBar);

        Bundle bundle = getArguments();
        post = (Post)bundle.getSerializable("Post");
        postNameTv.setText(post.getName());
        postDescTv.setText(post.getDescription());

        if (PostsListActivity.SIGNED_IN_USERNAME.equals(post.getPublisher())) {
            editButton = view.findViewById(R.id.postDetails__editButton);
            deleteButton = view.findViewById(R.id.postDetails__deleteButton);

            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);

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
                    DeletePostConfirmDialog dialog = new DeletePostConfirmDialog();
                    Bundle postToDeleteBundle = new Bundle();
                    postToDeleteBundle.putSerializable("PostToDelete", post);
                    dialog.setArguments(postToDeleteBundle);
                    dialog.show(getActivity().getSupportFragmentManager(), "TAG");
                }
            });
        } else {
            backButton = view.findViewById(R.id.postDetails__backButton);
            backButton.setVisibility(View.VISIBLE);

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onBack();
                }
            });
        }

        Model.instance.loadImage(post.getImageUrl(), new Model.OnFetchImageFromLocalCacheListener() {
            @Override
            public void onComplete(Bitmap pic) {
                if (pic != null) {
                    postImage.setImageBitmap(pic);
                } else {
                    postImage.setImageResource(R.drawable.about);
                }
                imageProgressBar.setVisibility(View.GONE);
            }
        });
    }

}
