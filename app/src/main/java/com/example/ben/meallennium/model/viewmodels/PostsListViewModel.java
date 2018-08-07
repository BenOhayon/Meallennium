package com.example.ben.meallennium.model.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;

import java.util.List;

public class PostsListViewModel extends ViewModel {

    public LiveData<List<Post>> getPostsData() {
        return Model.instance.getPostsData();
    }
}
