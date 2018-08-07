package com.example.ben.meallennium.model.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;

import java.util.List;

public class MyPostsListViewModel extends ViewModel {

    public LiveData<List<Post>> getMyPostsData() {
        return Model.instance.getMyPostsData();
    }
}
