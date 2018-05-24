package com.example.ben.meallennium.model;

import com.example.ben.meallennium.activities.MeallenniumApplication;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.model.sql.SqlModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Model {

    public static Model instance = new Model();
    private List<Post> postsData;
    private SqlModel sqlModel;
    private FirebaseModel firebaseModel;

    private Model() {
        postsData = new LinkedList<>();
        sqlModel = new SqlModel(MeallenniumApplication.context);
        firebaseModel = new FirebaseModel();
    }

    public void setListenerForFirebaseModel(FirebaseModel.FirebaseUserAuthListener listener) {
        firebaseModel.setFirebaseUserAuthListener(listener);
    }

    public void addUserToLocalDatabase(User user) {
        sqlModel.addUserEntry(user);
    }

    public void setPostsData(List<Post> list) {
        this.postsData = list;
    }

    public int getNumberOfPosts() {
        return postsData.size();
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

    public void setListenerForFirebaseUserDeleter(FirebaseModel.FirebaseUserDeleterListener listenerForFirebaseUserData) {
        firebaseModel.setFirebaseUserDeleterListener(listenerForFirebaseUserData);
    }

    public void setSignedInUserInFirebase(User user) {
        firebaseModel.setSignedInUser(user);
    }

    public void fetchAllPostsDataFromFirebase() {
        firebaseModel.fetchAllPostsData();
    }

    public void setListenerForFirebaseDataManager(FirebaseModel.FirebaseDataManagerListener listenerForFirebaseDataManager) {
        firebaseModel.setFirebaseDataManagerListener(listenerForFirebaseDataManager);
    }

    public void addPostToFirebase(Post post) {
        firebaseModel.createNewPost(post);
    }
}
