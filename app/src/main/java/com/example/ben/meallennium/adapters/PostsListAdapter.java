package com.example.ben.meallennium.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ben.meallennium.R;

public class PostsListAdapter extends RecyclerView.Adapter {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postItemName;

        public PostViewHolder(View itemView) {
            super(itemView);

            postItemName = itemView.findViewById(R.id.postItem__name);
        }

        // TODO continue with Udacity android course about RecyclerView.
    }
}
