package com.example.ben.meallennium.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.utils.LogTag;
import com.example.ben.meallennium.utils.Requests;
import com.example.ben.meallennium.utils.ToastMessageDisplayer;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class EditPostFragment extends Fragment {

    public interface OnCancelButtonClicked {
        void onCancel();
    }

    public interface OnEditCompleteListener {
        void onEditDone();
    }

    private ImageView postImage;
    private ProgressBar postImageProgressBar, centerProgressBar;
    private EditText postNameEt, postDescEt;
    private Bitmap postImageBitmap;

    private OnCancelButtonClicked cancelListener;
    private OnEditCompleteListener editListener;

    public EditPostFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnCancelButtonClicked && context instanceof OnEditCompleteListener) {
            cancelListener = (OnCancelButtonClicked)context;
            editListener = (OnEditCompleteListener)context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCancelButtonClicked and OnEditCompleteListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        initializeViewElements(view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Requests.IMAGE_CAPTURE_REQUEST && resultCode == RESULT_OK) {
            Log.d(LogTag.TAG, "Opening camera");
            postImageBitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            postImage.setImageBitmap(postImageBitmap);
            postImageProgressBar.setVisibility(View.GONE);
        }

        if(requestCode == Requests.IMAGE_FROM_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Log.d(LogTag.TAG, "Getting from gallery");
            if(data != null) {
                try{
                    postImageBitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), data.getData());
                    postImage.setImageBitmap(postImageBitmap);
                } catch(IOException e) {
                    ToastMessageDisplayer.displayToast(getActivity(), e.getMessage());
                }
            }
        }
    }

    private void initializeViewElements(View view) {
        postImage = view.findViewById(R.id.editPost__postImage);
        postImageProgressBar = view.findViewById(R.id.editPost__imageProgressBar);
        postImageProgressBar.setVisibility(View.VISIBLE);
        centerProgressBar = view.findViewById(R.id.editPost__centerProgressBar);

        Button takePictureButton = view.findViewById(R.id.editPost__takePictureButton);
        Button pickFromGalleryButton = view.findViewById(R.id.editPost__pickFromGalleryButton);
        Button OKButton = view.findViewById(R.id.editPost__OKButton);
        Button cancelButton = view.findViewById(R.id.editPost__cancelButton);

        postNameEt = view.findViewById(R.id.editPost__postNameEt);
        postDescEt = view.findViewById(R.id.editPost__postDescTextArea);

        Bundle bundle = getArguments();
        Post postToEdit = (Post) Objects.requireNonNull(bundle).get("postToEdit");
        postNameEt.setText(Objects.requireNonNull(postToEdit).getName());
        postDescEt.setText(postToEdit.getDescription());

        Model.instance.loadImage(postToEdit.getImageUrl(), (Bitmap pic) -> {
            if (pic != null) {
                postImageBitmap = pic;
                postImage.setImageBitmap(pic);
            } else {
                postImage.setImageResource(R.drawable.about);
            }
            postImageProgressBar.setVisibility(View.GONE);
        });

        takePictureButton.setOnClickListener((View v) -> {
            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, Requests.IMAGE_CAPTURE_REQUEST);
            }
        });

        pickFromGalleryButton.setOnClickListener((View v) -> {
            Intent chooseFromGallery = new Intent();
            chooseFromGallery.setType("image/*");
            chooseFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(chooseFromGallery, "Select Picture"), Requests.IMAGE_FROM_GALLERY_REQUEST);
        });

        OKButton.setOnClickListener((View v) -> {
            centerProgressBar.setVisibility(View.VISIBLE);
            Model.instance.saveImage(postImageBitmap, (String url) -> {
                Post newPost = new Post(Model.getSignedInUser(),
                        postNameEt.getText().toString(),
                        postDescEt.getText().toString());

                newPost.setId(postToEdit.getId());

                if(url != null) {
                    newPost.setImageUrl(url);
                }

                Model.instance.updatePost(Model.getSignedInUser(), postToEdit, newPost);
                editListener.onEditDone();
            });
        });

        cancelButton.setOnClickListener((View v) -> cancelListener.onCancel());
    }
}
