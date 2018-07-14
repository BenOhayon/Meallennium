package com.example.ben.meallennium.model.sql;

import android.os.AsyncTask;

import com.example.ben.meallennium.model.entities.Post;

import java.util.List;

public class PostAsyncDao {

    public static void getPostsByPublisher(String publisher, PostAsyncDaoListener<List<Post>> listener) {
        class MyAsyncTask extends AsyncTask<String, String, List<Post>> {
            @Override
            protected List<Post> doInBackground(String... strings) {
                return MillenniumDatabase.db.postDao().getPostsByPublisher(strings[0]);
            }

            @Override
            protected void onPostExecute(List<Post> posts) {
                super.onPostExecute(posts);

                if(listener != null) {
                    listener.onComplete(posts);
                }
            }
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute(publisher);
    }

    public static void addPosts(List<Post> posts, PostAsyncDaoListener<Boolean> listener) {
        class MyAsyncTask extends AsyncTask<List<Post>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<Post>... postList) {
                for (Post p : postList[0]) {
                    MillenniumDatabase.db.postDao().addPost(p);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean posts) {
                super.onPostExecute(posts);

                if(listener != null) {
                    listener.onComplete(posts);
                }
            }
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute(posts);
    }

    public interface PostAsyncDaoListener<T> {
        void onComplete(T result);
    }

    public static void getAllPosts(final PostAsyncDaoListener<List<Post>> listener) {
        class MyAsyncTask extends AsyncTask<String, String, List<Post>> {
            @Override
            protected List<Post> doInBackground(String... strings) {
                return MillenniumDatabase.db.postDao().getAllPosts();
            }

            @Override
            protected void onPostExecute(List<Post> posts) {
                super.onPostExecute(posts);
                if(listener != null) {
                    listener.onComplete(posts);
                }
            }
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public static void addPost(Post postToAdd, final PostAsyncDaoListener<Boolean> listener) {
        class MyAsyncTask extends AsyncTask<Post, String, Boolean> {
            @Override
            protected Boolean doInBackground(Post... postList) {
                MillenniumDatabase.db.postDao().addPost(postList[0]);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean posts) {
                super.onPostExecute(posts);

                if(listener != null) {
                    listener.onComplete(posts);
                }
            }
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute(postToAdd);
    }

    public static void deletePost(Post postToDelete, final PostAsyncDaoListener<Boolean> listener) {
        class MyAsyncTask extends AsyncTask<Post, String, Boolean> {
            @Override
            protected Boolean doInBackground(Post... post) {
                MillenniumDatabase.db.postDao().deletePost(post[0]);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean posts) {
                super.onPostExecute(posts);
                if(listener != null) {
                    listener.onComplete(posts);
                }
            }
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute(postToDelete);
    }

    public static void deleteAllPosts(List<Post> postsToDelete, final PostAsyncDaoListener<Boolean> listener) {
        class MyAsyncTask extends AsyncTask<List<Post>, String, Boolean> {
            @Override
            protected Boolean doInBackground(List<Post>... post) {
                for(Post p : post[0]) {
                    MillenniumDatabase.db.postDao().deletePost(p);
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean posts) {
                super.onPostExecute(posts);
                if(listener != null) {
                    listener.onComplete(posts);
                }
            }
        }

        MyAsyncTask task = new MyAsyncTask();
        task.execute(postsToDelete);
    }
}
