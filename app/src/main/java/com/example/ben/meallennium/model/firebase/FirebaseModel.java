package com.example.ben.meallennium.model.firebase;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class FirebaseModel {

    public void fetchAllPostsData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface FirebaseUserAuthListener {
        void onCreateUserSuccess(User user);
        void onCreateUserFailure(User user);
        void onSignInUserSuccess(User user);
        void onSignInUserFailure(User user);
    }

    public interface FirebaseUserDataListener {
        void onDeleteUser(User user);
    }

    private FirebaseUserAuthListener firebaseUserAuthListener;
    private FirebaseUserDataListener firebaseUserDataListener;
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private User signedInUser;

    public FirebaseModel() {
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public void setFirebaseUserAuthListener(FirebaseUserAuthListener firebaseUserAuthListener) {
        this.firebaseUserAuthListener = firebaseUserAuthListener;
    }

    public void setFirebaseUserDataListener(FirebaseUserDataListener firebaseUserDataListener) {
        this.firebaseUserDataListener = firebaseUserDataListener;
    }

    public void setSignedInUser(User user) {
        signedInUser = user;
    }

    public void createNewUser(User user) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if(task.isSuccessful()) {
                        Log.d("buildTest", "Firebase registration successful!");
                        firebaseUserAuthListener.onCreateUserSuccess(user);
                    } else {
                        Log.d("buildTest", "Firebase registration failed: " + task.getException());
                        firebaseUserAuthListener.onCreateUserFailure(user);
                    }
                });
    }

    public void signInUser(User user) {
        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener((Task<AuthResult> task) -> {
                    if(task.isSuccessful()) {
                        Log.d("buildTest", "Firebase sign in successful!");
                        firebaseUserAuthListener.onSignInUserSuccess(user);
                    } else {
                        Log.d("buildTest", "Firebase sign in failed: " + task.getException());
                        firebaseUserAuthListener.onSignInUserFailure(user);
                    }
                });
    }

    public void deleteSignedInUser() {
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
                        firebaseUserDataListener.onDeleteUser(signedInUser);
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
}
