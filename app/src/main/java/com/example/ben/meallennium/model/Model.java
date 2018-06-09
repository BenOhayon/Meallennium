package com.example.ben.meallennium.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.model.sql.SqlModel;
import com.example.ben.meallennium.model.sql.room_db_wrapper.PostAsyncDao;
import com.example.ben.meallennium.utils.ProgressBarManager;

import java.util.LinkedList;
import java.util.List;

public class Model {

    public static Model instance = new Model();
    private SqlModel sqlModel;
    private FirebaseModel firebaseModel;
    private PostsListLiveData postsData;

    class PostsListLiveData extends MutableLiveData<List<Post>> {

        public PostsListLiveData() {
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            sqlModel.getAllPosts(new PostAsyncDao.PostAsyncDaoListener<List<Post>>() {
                @Override
                public void onComplete(List<Post> result) {
                    Log.d("buildTest", "Got posts from local DB");
                    setValue(result);
                    firebaseModel.fetchAllPostsData(new FirebaseModel.OnFetchAllPostsListener() {
                        @Override
                        public void onComplete(List<Post> posts) {
                            Log.d("buildTest", "Got posts from Firebase");
                            setValue(posts);
                            ProgressBarManager.dismissProgressBar();

                            for(Post p : posts) {
                                sqlModel.addPost(p, new PostAsyncDao.PostAsyncDaoListener<Boolean>() {
                                    @Override
                                    public void onComplete(Boolean result) {
                                        Log.d("buildTest", "Posts saved in local DB");
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            firebaseModel.cancelFetchingData();
        }
    }


    private Model() {
        Log.d("buildTest", "Model is created");
        postsData = new PostsListLiveData();
        firebaseModel = new FirebaseModel();
        sqlModel = new SqlModel();
        postsData = new PostsListLiveData();
    }

    public void addPostToLocalDB(Post post, PostAsyncDao.PostAsyncDaoListener<Boolean> listener) {
        sqlModel.addPost(post, listener);
    }

    public void addUserToFirebase(User user, final FirebaseModel.OnCreateNewUserListener listener) {
        firebaseModel.registerNewUser(user, listener);
    }

    public void signInUserToFirebase(User user, final FirebaseModel.OnSignInUserListener listener) {
        firebaseModel.signInUser(user, listener);
    }

    public boolean isSignedInUserInFirebase() {
        return firebaseModel.isSignedInUser();
    }

    public void signOutCurrentUserFromFirebase() {
        firebaseModel.signOutCurrentUser();
    }

    public void deleteSignedInUserInFirebase(final FirebaseModel.OnUserDeleteListener listener) {
        firebaseModel.deleteSignedInUser(listener);
    }

    public void setSignedInUserInFirebase(User user) {
        firebaseModel.setSignedInUser(user);
    }

    public void addPostToFirebase(Post post, final FirebaseModel.OnCreateNewPostListener listener) {
        firebaseModel.createNewPost(post, listener);
    }

    public LiveData<List<Post>> getPostsData() {
        return postsData;
    }
}
