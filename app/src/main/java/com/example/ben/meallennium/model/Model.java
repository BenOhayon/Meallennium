package com.example.ben.meallennium.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.model.sql.SqlModel;
import com.example.ben.meallennium.model.sql.room_db_wrapper.PostAsyncDao;
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

public class Model {

    public interface OnOperationCompleteListener {
        void onComplete();
    }

    public static Model instance = new Model();
    private SqlModel sqlModel;
    private FirebaseModel firebaseModel;
    private PostsListLiveData postsData;

    class PostsListLiveData extends MutableLiveData<List<Post>> {

        int x = 0;

        public PostsListLiveData() {
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            sqlModel.getAllPosts(new PostAsyncDao.PostAsyncDaoListener<List<Post>>() {
                @Override
                public void onComplete(List<Post> postsFromDB) {
                    Log.d(LogTag.TAG, "Got " + postsFromDB.size() + " posts from local DB");
                    setValue(postsFromDB);
                    firebaseModel.fetchAllPostsData(new FirebaseModel.OnFetchAllPostsListener() {
                        @Override
                        public void onComplete(List<Post> postsFromFirebase) {
                            Log.d(LogTag.TAG, "Got " + postsFromFirebase.size() + " posts from Firebase");
                            List<Post> temp = new LinkedList<>();
                            temp.addAll(postsFromDB);
                            List<Post> deltaPostsList = getDeltaList(postsFromDB, postsFromFirebase);
                            Log.d(LogTag.TAG, "deltaList size: " + deltaPostsList.size());
                            if (deltaPostsList.size() != 0) {
                                temp.addAll(deltaPostsList);
                                setValue(temp);
                            }
                            sqlModel.addPosts(deltaPostsList, new PostAsyncDao.PostAsyncDaoListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean result) {
                                    Log.d(LogTag.TAG, "Posts saved in local DB");
                                    ProgressBarManager.dismissProgressBar();
                                }
                            });
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
        Log.d(LogTag.TAG, "Model is created");
        postsData = new PostsListLiveData();
        firebaseModel = new FirebaseModel();
        sqlModel = new SqlModel();
        postsData = new PostsListLiveData();
    }

    public void deletePost(Post post, final OnOperationCompleteListener listener) {
        sqlModel.deletePost(post, new PostAsyncDao.PostAsyncDaoListener<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                firebaseModel.deletePost(post, new FirebaseModel.OnDeletePostListener() {
                    @Override
                    public void onComplete() {
                        Log.d(LogTag.TAG, "A post " + post.getId() + " was deleted from local DB successfully");

                        Model.instance.postsData.getValue().remove(post);
                        Model.instance.postsData.setValue(Model.instance.postsData.getValue());

                        if(listener != null) {
                            listener.onComplete();
                        }
                    }
                });
            }
        });
    }

    public Bitmap fetchPostImageFromLocalCache(String imageUrl) {
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageUrl);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageUrl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

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

            //addPicureToGallery(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveImage(Bitmap image, final FirebaseModel.OnSaveImageListener listener) {
        firebaseModel.saveImage(image, listener);
    }

    public void addPostToLocalDB(Post post, PostAsyncDao.PostAsyncDaoListener<Boolean> listener) {
        sqlModel.addPost(post, listener);
    }

    public void deletePostFromLocalDB(Post post, final PostAsyncDao.PostAsyncDaoListener<Boolean> listener) {
        sqlModel.deletePost(post, listener);
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

    private List<Post> getDeltaList(List<Post> source, List<Post> getDeltaFrom) {
        List<Post> deltaList = new LinkedList<>();
        for(Post p : getDeltaFrom) {
            if(!source.contains(p)) {
                deltaList.add(p);
            }
        }
        return deltaList;
    }
}
