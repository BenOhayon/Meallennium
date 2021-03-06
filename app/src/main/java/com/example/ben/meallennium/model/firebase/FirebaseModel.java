package com.example.ben.meallennium.model.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.utils.LogTag;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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

    public interface OnGetImageListener {
        void onDone(Bitmap pic);
    }

    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private static ValueEventListener valueEventListener;
    private FirebaseAuth auth;
    private User signedInUser;

    public FirebaseModel() {
        auth = FirebaseAuth.getInstance();
    }

    public void saveImage(Bitmap image, final OnSaveImageListener listener) {
        if (image != null) {
            Date d = new Date();
            String name = "image-"+ d.getTime();
            StorageReference imagesRef = FirebaseStorage.getInstance().getReference()
                    .child("images").child(name);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imagesRef.putBytes(data);
            uploadTask.continueWithTask((Task<UploadTask.TaskSnapshot> task) -> {
                if(!task.isSuccessful()){
                    throw Objects.requireNonNull(task.getException());
                }

                return imagesRef.getDownloadUrl();
            }).addOnCompleteListener((Task<Uri> task) -> {
                if(task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d(LogTag.TAG, "Download Uri task success, download Uri: " + downloadUri);
                    listener.onDone(downloadUri.toString());
                } else {
                    Log.d(LogTag.TAG, "Download Uri task failed");
                }
            });
        } else {
            listener.onDone(null);
        }
    }

    public void getImage(String imageUrl, final OnGetImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(imageUrl);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener((byte[] bytes) -> {
            Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            Log.d("TAG","get image from firebase success");
            listener.onDone(image);
        }).addOnFailureListener((Exception exception) -> {
            Log.d("TAG",exception.getMessage());
            Log.d("TAG","get image from firebase Failed");
            listener.onDone(null);
        });
    }

    public void setSignedInUser(User user) {
        signedInUser = user;
    }

    public void registerNewUser(User user, final OnCreateNewUserListener listener) {
        DatabaseReference newUserRef = dbRef.child("Users").child(user.getUsername());
        newUserRef.setValue(user);

        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if(task.isSuccessful()) {
                        Log.d(LogTag.TAG, "Firebase registration successful!");
                        signedInUser = user;
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
                        signedInUser = user;
                        listener.onSignInComplete(user);
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
            user.reauthenticate(credential).addOnCompleteListener((Task<Void> task) -> Log.d(LogTag.TAG, "User re-authenticated."));

            user.delete().addOnCompleteListener((Task<Void> task) -> {
                if (task.isSuccessful()) {
                    Log.d(LogTag.TAG, "User account deleted.");
                    DatabaseReference userRef = dbRef.child("Posts").child(signedInUser.getUsername());
                    userRef.removeValue().addOnSuccessListener((Void aVoid) -> listener.onDeletionComplete(signedInUser));
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

    public void fetchPostsByPublisher(String publisher, OnFetchAllPostsListener listener) {
        DatabaseReference postsRef = dbRef.child("Posts").child(publisher);
        valueEventListener = postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> posts = new LinkedList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    posts.add(post);
                }

                Log.d(LogTag.TAG, "onDataChange() called on firebase");
                listener.onComplete(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(LogTag.TAG, "onCancelled() called in firebase");
                listener.onComplete(null);
            }
        });
    }

    public void fetchAllPosts(final OnFetchAllPostsListener listener) {
        DatabaseReference postsRef = dbRef.child("Posts");
        valueEventListener = postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> posts = new LinkedList<>();
                for(DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot : idSnapshot.getChildren()) {
                        Post post = postSnapshot.getValue(Post.class);
                        posts.add(post);
                    }
                }

                Log.d(LogTag.TAG, "onDataChange() called on firebase");
                listener.onComplete(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(LogTag.TAG, "onCancelled() called in firebase");
                listener.onComplete(null);
            }
        });
    }

    public void cancelFetchingData() {
        DatabaseReference ref = dbRef.child("Posts");
        ref.removeEventListener(valueEventListener);
    }

    public void deletePost(String publisher, Post post, final FirebaseModel.OnDeletePostListener listener) {
        Log.d(LogTag.TAG,post.getId() );
        DatabaseReference postRef = dbRef.child("Posts").child(publisher).child(post.getId());
        postRef.removeValue().addOnSuccessListener((Void aVoid) -> {
            Log.d(LogTag.TAG, "A post " + post.getId() + " was deleted from Firebase successfully");
            listener.onComplete();
        });
    }

    public void createNewPost(String publisher, Post post, final FirebaseModel.OnCreateNewPostListener listener) {
        Log.d(LogTag.TAG, "creating a new post in FirebaseModel." + post.getId());
        DatabaseReference postsRef = dbRef.child("Posts").child(publisher).child(post.getId());
        postsRef.setValue(post).addOnSuccessListener((Void aVoid) -> {
            Log.d(LogTag.TAG, "Create new post success!");
            if (listener != null) {
                listener.onComplete(post);
            }
        }).addOnFailureListener((Exception e) -> {
            Log.d(LogTag.TAG, "Creating a new post in Firebase failed.");
            if (listener != null) {
                listener.onComplete(null);
            }
        });
    }
}
