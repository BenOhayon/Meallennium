package com.example.ben.meallennium.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.model.viewmodels.PostsListViewModel;
import com.example.ben.meallennium.utils.LogTag;
import com.example.ben.meallennium.utils.ProgressBarManager;

import java.util.List;

public class PostsListFragment extends Fragment {

    private PostsListViewModel postsViewModel;
    private PostsListAdapter adapter;
    private PostsListFragmentListener listener;

    public interface PostsListFragmentListener {
        void onListItemSelect(int clickedItemIndex);
    }

    interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

        private ListItemClickListener listener;

        public PostsListAdapter(ListItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_list_item,
                    parent, false);

            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PostsListAdapter.PostViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return Model.instance.getPostsData().getValue().size();
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

                // TODO Figure out how to load the pictures from local cache to the ImageView of the Post items.
                imageProgressBar.setVisibility(View.VISIBLE);
                Post post = Model.instance.getPostsData().getValue().get(listIndex);
                postItemName.setText(post.getName());
                postImage.setImageResource(R.drawable.add);
                Model.instance.loadImage(post.getImageUrl(), new Model.OnFetchImageFromLocalCacheListener() {
                    @Override
                    public void onComplete(Bitmap pic) {
                        Log.d(LogTag.TAG, "Retrieving picture from local cache");
                        if (pic != null) {
                            postImage.setImageBitmap(pic);
                            imageProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onClick(View v) {
                int clickedPosition = getAdapterPosition();
                listener.onListItemClick(clickedPosition);
            }
        }
    }

    public PostsListFragment() {}

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
        adapter = new PostsListAdapter(new ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex) {
                handleListItemClick(clickedItemIndex);
            }
        });
        postsList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof PostsListFragmentListener) {
            listener = (PostsListFragmentListener ) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PostsListFragmentListener");
        }

        postsViewModel = ViewModelProviders.of(this).get(PostsListViewModel.class);
        postsViewModel.getPostsData().observe(this, new Observer<List<Post>>() {
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

    public void handleListItemClick(int clickedItemIndex) {
        listener.onListItemSelect(clickedItemIndex);
    }
}
