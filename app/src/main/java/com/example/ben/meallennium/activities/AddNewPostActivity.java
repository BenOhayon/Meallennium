package com.example.ben.meallennium.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.utils.LogTag;
import com.example.ben.meallennium.utils.ProgressBarManager;
import com.example.ben.meallennium.utils.Requests;
import com.example.ben.meallennium.utils.Results;
import com.example.ben.meallennium.utils.ToastMessageDisplayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class AddNewPostActivity extends AppCompatActivity {

    private ImageView postImage;
    private Bitmap bitmapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        postImage = findViewById(R.id.createPostScreen__imageSelect);
        Button postButton = findViewById(R.id.createPostScreen__postButton);
        Button cancelButton = findViewById(R.id.createPostScreen__cancelButton);
        Button takePictureButton = findViewById(R.id.createPostScreen__takePictureButton);
        Button pickGalleryButton = findViewById(R.id.createPostScreen__pickFromGalleryButton);
        EditText postNameEt = findViewById(R.id.createPostScreen__postNameEt);
        EditText postDescEt = findViewById(R.id.createPostScreen__postDescTextArea);
        ProgressBar loadingProgressBar = findViewById(R.id.createPostScreen__progressBar);
        ProgressBarManager.bindProgressBar(loadingProgressBar);

        takePictureButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, Requests.IMAGE_CAPTURE_REQUEST);
            }
        });

        pickGalleryButton.setOnClickListener((View v) -> {
            Intent chooseFromGallery = new Intent();
            chooseFromGallery.setType("image/*");
            chooseFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(chooseFromGallery, "Select Picture"), Requests.IMAGE_FROM_GALLERY_REQUEST);
        });

        postButton.setOnClickListener((View v) -> {
            ProgressBarManager.showProgressBar();
            String name = postNameEt.getText().toString();
            String desc = postDescEt.getText().toString();

            Intent newPostIntent = getIntent();
            newPostIntent.putExtra("postName", name);
            newPostIntent.putExtra("postDesc", desc);

            if(bitmapImage != null) {
                Model.instance.saveImage(bitmapImage, new FirebaseModel.OnSaveImageListener() {
                    @Override
                    public void onDone(String url) {
                        if(url != null) {
                            Log.d(LogTag.TAG, "save image in firebase success, imageURL: " + url);
                            newPostIntent.putExtra("imageURL", url);
                            Model.instance.savePostImageToLocalCache(bitmapImage, url);
                            Log.d(LogTag.TAG, "save image in local cache success");
                            setResult(Results.POST_CREATION_SUCCESS, newPostIntent);
                            finish();
                        } else {
                            Log.d(LogTag.TAG, "save image in firebase failed");
                        }
                    }
                });
            } else {
                setResult(Results.POST_CREATION_SUCCESS, newPostIntent);
                finish();
            }
        });

        cancelButton.setOnClickListener((View v) -> {
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Requests.IMAGE_CAPTURE_REQUEST
                && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmapImage = (Bitmap) extras.get("data");

        } else if(requestCode == Requests.IMAGE_FROM_GALLERY_REQUEST
                && resultCode == RESULT_OK) {
            if(data != null) {
                try{
                    bitmapImage = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), data.getData());
                } catch(IOException e) {
                    ToastMessageDisplayer.displayToast(this, e.getMessage());
                }
            }
        }

        if (bitmapImage != null) {
            postImage.setImageBitmap(bitmapImage);
        } else {
            postImage.setImageResource(R.drawable.about);
        }
    }

    private Bitmap getRotatedImage(String imageFileUrl) {
        Log.d(LogTag.TAG, "image file url got for ExifInterface: " + imageFileUrl);
        ExifInterface ei = null;
        Bitmap rotatedBitmap = null;

        try {
            ei = new ExifInterface(imageFileUrl);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.d(LogTag.TAG, "image rotated 90 degrees");
                    rotatedBitmap = rotateImage(bitmapImage, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.d(LogTag.TAG, "image rotated 180 degrees");
                    rotatedBitmap = rotateImage(bitmapImage, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.d(LogTag.TAG, "image rotated 270 degrees");
                    rotatedBitmap = rotateImage(bitmapImage, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    Log.d(LogTag.TAG, "image didn't rotate");
                    rotatedBitmap = bitmapImage;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotatedBitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
