package com.example.ben.meallennium.model;

import android.content.SharedPreferences;

import com.example.ben.meallennium.activities.MeallenniumApplication;
import com.example.ben.meallennium.activities.PostsListActivity;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.model.sql.SqlModel;

import java.util.ArrayList;
import java.util.List;

public class Model {

    public static Model instnace = new Model();
    private List<User> usersData;
    private SqlModel sqlModel;
    private FirebaseModel firebaseModel;

    private Model() {
        this.usersData = new ArrayList<>();
        sqlModel = new SqlModel(MeallenniumApplication.context);
        firebaseModel = new FirebaseModel();
    }

    public void setListenerForFirebaseModel(FirebaseModel.FirebaseUserAuthListener listener) {
        firebaseModel.setFirebaseUserAuthListener(listener);
    }

    public void addUserToLocalDatabase(User user) {
        sqlModel.addUserEntry(user);
    }

    public int getNumberOfUsers() {
        return usersData.size();
    }

    public void popUpAllUsers() {
        sqlModel.popUpAllUserEntries();
    }

    public void addUserToFirebase(User user) {
        firebaseModel.createNewUser(user);
    }

    public void signInUserToFirebase(User user) {
        firebaseModel.signInUser(user);
    }

    public boolean isSignedInUserInFirebase() {
        return firebaseModel.isSignedInUser();
    }

    public void signOutCurrentUserFromFirebase() {
        firebaseModel.signOutCurrentUser();
    }

    public void deleteSignedInUserInFirebase() {
        firebaseModel.deleteSignedInUser();
    }

    public void setListenerForFirebaseUserData(FirebaseModel.FirebaseUserDataListener listenerForFirebaseUserData) {
        firebaseModel.setFirebaseUserDataListener(listenerForFirebaseUserData);
    }

    public void setSignedInUserInFirebase(User user) {
        firebaseModel.setSignedInUser(user);
    }
}
