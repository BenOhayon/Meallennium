package com.example.ben.meallennium.model.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.utils.ToastMessageDisplayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.LinkedList;
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

    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private User signedInUser;

    private ValueEventListener valueEventListener;

    public FirebaseModel() {
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public void setSignedInUser(User user) {
        signedInUser = user;
    }

    public void registerNewUser(User user, final OnCreateNewUserListener listener) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if(task.isSuccessful()) {
                        Log.d("buildTest", "Firebase registration successful!");
                        listener.onCreationComplete(user);
                    } else {
                        Log.d("buildTest", "Firebase registration failed: " + task.getException());
                        listener.onCreationComplete(null);
                    }
                });
    }

    public void signInUser(User user, final OnSignInUserListener listener) {
        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if(task.isSuccessful()) {
                        Log.d("buildTest", "Firebase sign in successful!");
                        listener.onSignInComplete(user);
                        signedInUser = user;
                    } else {
                        Log.d("buildTest", "Firebase sign in failed: " + task.getException());
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
                    Log.d("buildTest", "User re-authenticated.");
                }
            });

            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("buildTest", "User account deleted.");
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

    public void fetchAllPostsData(final OnFetchAllPostsListener listener) {
        DatabaseReference postsRef = dbRef.child("Posts");
        valueEventListener = postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> posts = new LinkedList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    posts.add(post);
                }

                Log.d("buildTest", "onDataChange() called on firebase");
                listener.onComplete(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("buildTest", "onCancelled() called in firebase");
                listener.onComplete(null);
            }
        });
    }

    public void cancelFetchingData() {
        DatabaseReference ref = dbRef.child("Posts");
        ref.removeEventListener(valueEventListener);
    }

    public void createNewPost(Post post, final OnCreateNewPostListener listener) {
        Log.d("buildTest", "creating a new post in FirebaseModel.");
        DatabaseReference postsRef = dbRef.child("Posts").child(post.getId());
        postsRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("buildTest", "Create new post success!");
                listener.onComplete(post);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("buildTest", "Creating a new post in Firebase failed.");
                listener.onComplete(null);
            }
        });
    }
}
