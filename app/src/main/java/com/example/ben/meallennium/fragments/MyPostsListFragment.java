package com.example.ben.meallennium.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.activities.PostsListActivity;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.model.sql.PostAsyncDao;
import com.example.ben.meallennium.model.viewmodels.MyPostsListViewModel;
import com.example.ben.meallennium.utils.LogTag;
import com.example.ben.meallennium.utils.ProgressBarManager;
import com.example.ben.meallennium.utils.Requests;
import com.example.ben.meallennium.utils.Results;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyPostsListFragment extends Fragment {

    public interface MyPostsListFragmentListener {
        void onMyListItemClick(int clickedItemIndex);
        void onAddFabClick();
    }

    private MyPostsListViewModel postsViewModel;
    private MyPostsListAdapter adapter;
    private FloatingActionButton addFab;

    private MyPostsListFragmentListener listener;

    class MyPostsListAdapter extends RecyclerView.Adapter<MyPostsListAdapter.PostViewHolder> {

        private MyPostsListFragmentListener listener;

        public MyPostsListAdapter(MyPostsListFragmentListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_list_item,
                    parent, false);

            return new MyPostsListAdapter.PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return Model.instance.getMyPostsData().getValue().size();
        }


        public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView postItemName;
            ImageView postImage;
            ProgressBar imageProgressBar;

            private PostViewHolder(View itemView) {
                super(itemView);

                postItemName = itemView.findViewById(R.id.postItem_name);
                postImage = itemView.findViewById(R.id.postItem_image);
                imageProgressBar = itemView.findViewById(R.id.postItem_progressBar);
                itemView.setOnClickListener(this);
            }

            private void bind(int listIndex) {
                imageProgressBar.setVisibility(View.VISIBLE);
                Post post = Model.instance.getMyPostsData().getValue().get(listIndex);
                postItemName.setText(post.getName());

                if (post.getImageUrl() != null) {
                    Model.instance.loadImage(post.getImageUrl(), new Model.OnFetchImageFromLocalCacheListener() {
                        @Override
                        public void onComplete(Bitmap pic) {
                            Log.d(LogTag.TAG, "Retrieving picture from local cache");
                            if (pic != null) {
                                postImage.setImageBitmap(pic);
                            } else {
                                postImage.setImageResource(R.drawable.about);
                            }
                            imageProgressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    postImage.setImageResource(R.drawable.about);
                    imageProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onClick(View v) {
                int clickedPosition = getAdapterPosition();
                listener.onMyListItemClick(clickedPosition);
            }
        }
    }

    public MyPostsListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);

        RecyclerView postsList = view.findViewById(R.id.postsListScreen__list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        postsList.setLayoutManager(layoutManager);
        postsList.setHasFixedSize(true);
        adapter = new MyPostsListAdapter((MyPostsListFragmentListener) getActivity());
        postsList.setAdapter(adapter);

//        addFab = view.findViewById(R.id.postsListScreen__addFab);
//        addFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onAddFabClick();
//            }
//        });

        return view;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == Requests.NEW_POST_REQUEST) {
//            if(resultCode == Results.POST_CREATION_SUCCESS) {
//                ProgressBarManager.showProgressBar();
//                String postName = data.getStringExtra("postName");
//                String postDesc = data.getStringExtra("postDesc");
//                String imageUrl = data.getStringExtra("imageURL");
//                Post post = new Post(PostsListActivity.SIGNED_IN_USERNAME, postName, postDesc);
//
//                if(imageUrl != null) {
//                    post.setImageUrl(imageUrl);
//                }
//
//                Model.instance.addPost(getActivity().getSharedPreferences("SP", MODE_PRIVATE).getString("userName", "default name"),
//                        post, new FirebaseModel.OnCreateNewPostListener() {
//                            @Override
//                            public void onComplete(Post post) {
//                                ProgressBarManager.dismissProgressBar();
//                            }
//                        }, new PostAsyncDao.PostAsyncDaoListener<Boolean>() {
//                            @Override
//                            public void onComplete(Boolean result) {
//                                Log.d(LogTag.TAG, "New post " + post.getId() + " was added to local DB.");
//                            }
//                        });
//
////                Model.instance.addPostToFirebase(getActivity().getSharedPreferences("SP", MODE_PRIVATE).getString("userName", "default name"), post, new FirebaseModel.OnCreateNewPostListener() {
////                    @Override
////                    public void onComplete(Post post) {
////                        Model.instance.addPost(post, null);
////                        ProgressBarManager.dismissProgressBar();
////                    }
////                });
//            }
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof MyPostsListFragmentListener) {
            listener = (MyPostsListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MyPostsListFragmentListener");
        }

        postsViewModel = ViewModelProviders.of(this).get(MyPostsListViewModel.class);
        postsViewModel.getMyPostsData().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {
                adapter.notifyDataSetChanged();
                Log.d("buildTest", "LiveData has updated");
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}