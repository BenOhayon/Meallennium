package com.example.ben.meallennium.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.model.sql.PostAsyncDao;
import com.example.ben.meallennium.utils.LogTag;
import com.example.ben.meallennium.utils.ProgressBarManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Model {

    public interface OnOperationCompleteListener {
        void onComplete();
    }

    public interface OnFetchImageFromLocalCacheListener {
        void onComplete(Bitmap pic);
    }

    public static Model instance = new Model();
    private FirebaseModel firebaseModel;
    private PostsListLiveData postsData;
    private MyPostsListLiveData myPostsData;

    private static String SIGNED_IN_USERNAME;

    public class MyPostsListLiveData extends MutableLiveData<List<Post>> {
        MyPostsListLiveData() {
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            PostAsyncDao.getPostsByPublisher(SIGNED_IN_USERNAME, (List<Post> result) -> {
                        if (result.size() != 0) {
                            setValue(result);
                        } else {
                            firebaseModel.fetchPostsByPublisher(SIGNED_IN_USERNAME, (List<Post> posts) -> {
                                if(posts != null) {
                                    setValue(posts);
                                }
                            });
                        }
                    });
        }
    }

    public class PostsListLiveData extends MutableLiveData<List<Post>> {

        PostsListLiveData() {
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            PostAsyncDao.getAllPosts((List<Post> postsFromDB) -> {
                setValue(postsFromDB);
                firebaseModel.fetchAllPosts((List<Post> postsFromFirebase) -> {
                    if(postsFromFirebase != null && postsFromFirebase.size() != 0) {
                        setValue(postsFromFirebase);
                    }

                    ProgressBarManager.dismissProgressBar();
                    PostAsyncDao.deleteAllPosts(postsFromDB,
                            (Boolean result) -> PostAsyncDao.addPosts(postsFromFirebase,
                            (Boolean result1) -> Log.d(LogTag.TAG, "Posts saved in local DB")));
                });
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            firebaseModel.cancelFetchingData();
        }
    }

    private Model() {
        Log.d(LogTag.TAG, "Model is created");
        postsData = new PostsListLiveData();
        myPostsData = new MyPostsListLiveData();
        firebaseModel = new FirebaseModel();
        postsData = new PostsListLiveData();
    }

    public static void setSignedInUser(String username) {
        SIGNED_IN_USERNAME = username;
    }

    public static String getSignedInUser() {
        return SIGNED_IN_USERNAME;
    }

    public void updatePost(String publisher, Post oldPost, Post newPost) {
        PostAsyncDao.deletePost(oldPost, (Boolean result) -> firebaseModel.createNewPost(publisher, newPost, null));
    }

    public void deletePost(Post post, final OnOperationCompleteListener listener) {
        PostAsyncDao.deletePost(post,
                (Boolean result) -> firebaseModel.deletePost(SIGNED_IN_USERNAME, post, () -> {
                    Log.d(LogTag.TAG, "A post " + post.getId() + " was deleted from local DB successfully");

                    Objects.requireNonNull(Model.instance.postsData.getValue()).remove(post);
                    Model.instance.postsData.setValue(Model.instance.postsData.getValue());

                    if(listener != null) {
                        listener.onComplete();
                    }
                }));
    }

    public void loadImage(String imageUrl, final OnFetchImageFromLocalCacheListener listener){
        if (imageUrl != null) {
            String localFileName = URLUtil.guessFileName(imageUrl, null, null);
            final Bitmap image = fetchPostImageFromLocalCache(localFileName);
            if (image == null) {
                firebaseModel.getImage(imageUrl, (Bitmap pic) -> {
                    if (pic == null) {
                        listener.onComplete(null);
                    } else {
                        String localFileName1 = URLUtil.guessFileName(imageUrl, null, null);
                        Log.d("TAG", "save image to cache: " + localFileName1);
                        savePostImageToLocalCache(pic, localFileName1);
                        listener.onComplete(pic);
                    }
                });
            }else {
                Log.d("TAG","OK reading cache image: " + localFileName);
                listener.onComplete(image);
            }
        } else {
            listener.onComplete(null);
        }
    }

    private Bitmap fetchPostImageFromLocalCache(String imageUrl) {
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageUrl);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d(LogTag.TAG,"got image from cache: " + imageUrl + ", is null: " + (bitmap == null));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void savePostImageToLocalCache(Bitmap imageBitmap, String imageUrl) {
        if (imageBitmap == null) return;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageUrl);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveImage(Bitmap image, final FirebaseModel.OnSaveImageListener listener) {
        firebaseModel.saveImage(image, listener);
    }

    public void addPost(String publisher, Post post, final FirebaseModel.OnCreateNewPostListener firebaseListener,
                        PostAsyncDao.PostAsyncDaoListener<Boolean> DBListener) {
        firebaseModel.createNewPost(publisher, post, firebaseListener);
        PostAsyncDao.addPost(post, DBListener);
    }

    public void addUser(User user, final FirebaseModel.OnCreateNewUserListener listener) {
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

    public void deleteUser(final FirebaseModel.OnUserDeleteListener listener) {
        firebaseModel.deleteSignedInUser(listener);
    }

    public void setSignedInUserInFirebase(User user) {
        firebaseModel.setSignedInUser(user);
    }

    public LiveData<List<Post>> getPostsData() {
        return postsData;
    }

    public LiveData<List<Post>> getMyPostsData() {
        return myPostsData;
    }
}
