package com.example.ben.meallennium.model.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.utils.LogTag;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class PostFirebase {

    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private static ValueEventListener valueEventListener;


    public static void fetchAllPosts(final FirebaseModel.OnFetchAllPostsListener listener) {
        DatabaseReference postsRef = dbRef.child("Posts");
        valueEventListener = postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

    public static void cancelFetchingData() {
        DatabaseReference ref = dbRef.child("Posts");
        ref.removeEventListener(valueEventListener);
    }

    public static void deletePost(Post post, final FirebaseModel.OnDeletePostListener listener) {
        Log.d(LogTag.TAG,post.getId() );
        DatabaseReference postRef = dbRef.child("Posts").child(post.getId());
        postRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(LogTag.TAG, "A post " + post.getId() + " was deleted from Firebase successfully");
                listener.onComplete();
            }
        });
    }

    public static void createNewPost(Post post, final FirebaseModel.OnCreateNewPostListener listener) {
        Log.d(LogTag.TAG, "creating a new post in FirebaseModel." + post.getId());
        DatabaseReference postsRef = dbRef.child("Posts").child(post.getId());
        postsRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(LogTag.TAG, "Create new post success!");
                listener.onComplete(post);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(LogTag.TAG, "Creating a new post in Firebase failed.");
                        listener.onComplete(null);
                    }
                });
    }
}
