package com.example.ben.meallennium.model.firebase;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.utils.LogTag;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

public class FirebaseModel {

    public interface OnUserDeleteListener {
        void onDeletionComplete(User user);
    }

    public interface OnCreateNewUserListener {
        void onCreationComplete(User user);
    }

    public interface OnSignInUserListener {
        void onSignInComplete(User user);
    }

    public interface OnCreateNewPostListener {
        void onComplete(Post post);
    }

    public interface OnFetchAllPostsListener {
        void onComplete(List<Post> posts);
    }

    public interface OnSaveImageListener {
        void onDone(String url);
    }

    public interface OnDeletePostListener {
        void onComplete();
    }

    private FirebaseAuth auth;
    private User signedInUser;

    public FirebaseModel() {
        auth = FirebaseAuth.getInstance();
    }

    public void saveImage(Bitmap image, final OnSaveImageListener listener) {
        Date d = new Date();
        String name = "image-"+ d.getTime();
        StorageReference imagesRef = FirebaseStorage.getInstance().getReference()
                .child("images").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }

                return imagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d(LogTag.TAG, "Download Uri task success, download Uri: " + downloadUri);
                    listener.onDone(downloadUri.toString());
                } else {
                    Log.d(LogTag.TAG, "Download Uri task failed");
                }
            }
        });
    }

    public void setSignedInUser(User user) {
        signedInUser = user;
    }

    public void registerNewUser(User user, final OnCreateNewUserListener listener) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if(task.isSuccessful()) {
                        Log.d(LogTag.TAG, "Firebase registration successful!");
                        listener.onCreationComplete(user);
                    } else {
                        Log.d(LogTag.TAG, "Firebase registration failed: " + task.getException());
                        listener.onCreationComplete(null);
                    }
                });
    }

    public void signInUser(User user, final OnSignInUserListener listener) {
        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if(task.isSuccessful()) {
                        Log.d(LogTag.TAG, "Firebase sign in successful!");
                        listener.onSignInComplete(user);
                        signedInUser = user;
                    } else {
                        Log.d(LogTag.TAG, "Firebase sign in failed: " + task.getException());
                        listener.onSignInComplete(null);
                    }
                });
    }

    public void deleteSignedInUser(final OnUserDeleteListener listener) {
        FirebaseUser user = auth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(signedInUser.getEmail(), signedInUser.getPassword());

        if(user != null) {
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(LogTag.TAG, "User re-authenticated.");
                }
            });

            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(LogTag.TAG, "User account deleted.");
                        listener.onDeletionComplete(signedInUser);
                        signedInUser = null;
                    }
                }
            });
        }
    }

    public boolean isSignedInUser() {
        FirebaseUser signedInUser = auth.getCurrentUser();
        return signedInUser != null;
    }

    public void signOutCurrentUser() {
        auth.signOut();
    }

    public void fetchAllPosts(final OnFetchAllPostsListener listener) {
        PostFirebase.fetchAllPosts(listener);
    }

    public void cancelFetchingData() {
        PostFirebase.cancelFetchingData();
    }

    public void deletePost(Post post, final OnDeletePostListener listener) {
        PostFirebase.deletePost(post, listener);
    }

    public void createNewPost(Post post, final OnCreateNewPostListener listener) {
        PostFirebase.createNewPost(post, listener);
    }
}
