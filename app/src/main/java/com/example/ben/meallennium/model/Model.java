package com.example.ben.meallennium.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.ben.meallennium.activities.PostsListActivity;
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

// TODO Improve the design in the app screens.
// TODO Figure out if CardView can help improving design.

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

    public class MyPostsListLiveData extends MutableLiveData<List<Post>> {
        public MyPostsListLiveData() {
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            PostAsyncDao.getPostsByPublisher(PostsListActivity.SIGNED_IN_USERNAME,
                    new PostAsyncDao.PostAsyncDaoListener<List<Post>>() {
                        @Override
                        public void onComplete(List<Post> result) {
                            // TODO Understand how DAO queries work.
                            Log.d(LogTag.TAG, "Posts loaded from local DB. Inside onActive()");
                            for(Post p : result) {
                                Log.d(LogTag.TAG, "Post ID: " + p.getId() + "\nPost name: " + p.getName() + "\nPost Description: " + p.getDescription() + "\nPost Url: " + p.getImageUrl());
                                Log.d(LogTag.TAG, "===================================");
                            }
                            Log.d(LogTag.TAG, "Post list loaded from DB size: " + result.size());
                            setValue(result);
                            Log.d(LogTag.TAG, "My Posts were successfully loaded from the local DB");
                        }
                    });
        }
    }

    public class PostsListLiveData extends MutableLiveData<List<Post>> {

        public PostsListLiveData() {
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            PostAsyncDao.getAllPosts(new PostAsyncDao.PostAsyncDaoListener<List<Post>>() {
                @Override
                public void onComplete(List<Post> postsFromDB) {
                    Log.d(LogTag.TAG, "Got " + postsFromDB.size() + " posts from local DB");
                    Log.d(LogTag.TAG, "The posts gor from DB are:\n");
                    for(Post p: postsFromDB) {
                        Log.d(LogTag.TAG, "Post ID: " + p.getId() + "\nPost name: " + p.getName() + "\nPost Description: " + p.getDescription() + "\nPost Url: " + p.getImageUrl());
                        Log.d(LogTag.TAG, "===================================");
                    }
                    Log.d(LogTag.TAG, "\n\nSetting value of posts list\n\n");
                    setValue(postsFromDB);
                    firebaseModel.fetchAllPosts(new FirebaseModel.OnFetchAllPostsListener() {
                        @Override
                        public void onComplete(List<Post> postsFromFirebase) {
                            Log.d(LogTag.TAG, "Updating the posts list from Firebase");
                            Log.d(LogTag.TAG, "Got " + postsFromFirebase.size() + " posts from Firebase");
                            Log.d(LogTag.TAG, "\n\nThe posts got from Firebase are:\n");
                            for(Post p : postsFromFirebase) {
                                Log.d(LogTag.TAG, "Post ID: " + p.getId() + "\nPost name: " + p.getName() + "\nPost Description: " + p.getDescription() + "\nPost Url: " + p.getImageUrl());
                                Log.d(LogTag.TAG, "===================================");
                            }
                            List<Post> temp = new LinkedList<>();
                            temp.addAll(postsFromDB);
                            List<Post> deltaPostsList = getDeltaList(postsFromDB, postsFromFirebase);
                            Log.d(LogTag.TAG, "\n\n\nThe posts in the Delta are:\n");
                            for(Post p : deltaPostsList) {
                                Log.d(LogTag.TAG, "Post ID: " + p.getId() + "\nPost name: " + p.getName() + "\nPost Description: " + p.getDescription() + "\nPost Url: " + p.getImageUrl());
                                Log.d(LogTag.TAG, "===================================");
                            }
                            Log.d(LogTag.TAG, "deltaList size: " + deltaPostsList.size());
                            if (deltaPostsList.size() != 0) {
                                for (Post p : deltaPostsList) {
                                    Post postById = getPostFromListById(temp, p.getId());
                                    int postByIdIndex = temp.indexOf(postById);
                                    if(postById != null) {
                                        temp.remove(postById);
                                    }

                                    if (postByIdIndex > -1) {
                                        temp.set(postByIdIndex, p);
                                    } else {
                                        temp.add(p);
                                    }
                                }
                                setValue(temp);
                            }
                            ProgressBarManager.dismissProgressBar();
                            PostAsyncDao.deleteAllPosts(postsFromDB, new PostAsyncDao.PostAsyncDaoListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean result) {
                                    PostAsyncDao.addPosts(temp, new PostAsyncDao.PostAsyncDaoListener<Boolean>() {
                                        @Override
                                        public void onComplete(Boolean result) {
                                            Log.d(LogTag.TAG, "Posts saved in local DB");
                                        }
                                    });
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
        myPostsData = new MyPostsListLiveData();
        firebaseModel = new FirebaseModel();
        postsData = new PostsListLiveData();
    }

    public void updatePost(String publisher, Post newPost) {
        firebaseModel.updatePost(publisher, newPost);
    }

    public void deletePost(Post post, final OnOperationCompleteListener listener) {
        PostAsyncDao.deletePost(post, new PostAsyncDao.PostAsyncDaoListener<Boolean>() {
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

    public void loadImage(String imageUrl, final OnFetchImageFromLocalCacheListener listener){
        if (imageUrl != null) {
            String localFileName = URLUtil.guessFileName(imageUrl, null, null);
            final Bitmap image = fetchPostImageFromLocalCache(localFileName);
            if (image == null) {
                firebaseModel.getImage(imageUrl, new FirebaseModel.OnGetImageListener() {
                    @Override
                    public void onDone(Bitmap pic) {
                        if (pic == null) {
                            listener.onComplete(null);
                        } else {
                            String localFileName = URLUtil.guessFileName(imageUrl, null, null);
                            Log.d("TAG", "save image to cache: " + localFileName);
                            savePostImageToLocalCache(pic, localFileName);
                            listener.onComplete(pic);
                        }
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

    public void addPost(String publisher, Post post, final FirebaseModel.OnCreateNewPostListener firebaseListener,
                        PostAsyncDao.PostAsyncDaoListener<Boolean> DBListener) {
        addPostToFirebase(publisher, post, firebaseListener);
        PostAsyncDao.addPost(post, DBListener);
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

    public LiveData<List<Post>> getPostsData() {
        return postsData;
    }

    public LiveData<List<Post>> getMyPostsData() {
        return myPostsData;
    }

    private void addPostToFirebase(String publisher, Post post, final FirebaseModel.OnCreateNewPostListener listener) {
        firebaseModel.createNewPost(publisher, post, listener);
    }

    private List<Post> getDeltaList(List<Post> source, List<Post> getDeltaFrom) {
        Log.d(LogTag.TAG, "\nInside getDeltaList():");
        List<Post> deltaList = new LinkedList<>();
        for(Post p : getDeltaFrom) {
            Log.d(LogTag.TAG, "Post ID: " + p.getId() + "\nPost name: " + p.getName() + "\nPost Description: " + p.getDescription() + "\nPost Url: " + p.getImageUrl());
            Log.d(LogTag.TAG, "DB has post: " + (source.contains(p)));
            if(!source.contains(p)) {
                deltaList.add(p);
                Log.d(LogTag.TAG, "The post bellow was added to the delta:");
                Log.d(LogTag.TAG, "Post ID: " + p.getId() + "\nPost name: " + p.getName() + "\nPost Description: " + p.getDescription() + "\nPost Url: " + p.getImageUrl());
            }
        }
        Log.d(LogTag.TAG, "returning from getDeltaList()\n");
        return deltaList;
    }

    private Post getPostFromListById(List<Post> temp, String id) {
        for(Post p : temp) {
            if(p.getId().equals(id))
                return p;
        }

        return null;
    }
}
