package com.example.ben.meallennium.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.model.entities.Post;

import java.util.LinkedList;
import java.util.List;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    private ListItemClickListener listener;
    private List<Post> posts;

    public PostsListAdapter(ListItemClickListener listener) {
        posts = new LinkedList<>();
        this.listener = listener;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_list_item,
                parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public List<Post> getPosts() {
        return this.posts;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView postItemName;
        ImageView postImage;

        public PostViewHolder(View itemView) {
            super(itemView);

            postItemName = itemView.findViewById(R.id.postItem_name);
            postImage = itemView.findViewById(R.id.postItem_image);
            itemView.setOnClickListener(this);
        }

        private void bind(int listIndex) {
            postItemName.setText(posts.get(listIndex).getName());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listener.onListItemClick(clickedPosition);
        }
    }
}
