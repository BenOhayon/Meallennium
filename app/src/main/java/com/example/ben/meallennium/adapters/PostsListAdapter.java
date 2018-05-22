package com.example.ben.meallennium.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ben.meallennium.R;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    private ListItemClickListener listener;
    private int numberOfPosts;

    public PostsListAdapter(int items, ListItemClickListener listener) {
        numberOfPosts = items;
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
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numberOfPosts;
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

        public void bind(int listIndex) {
            postItemName.setText(String.valueOf(listIndex));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listener.onListItemClick(clickedPosition);
        }
    }
}
