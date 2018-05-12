package com.example.ben.meallennium.model;

import com.example.ben.meallennium.model.entities.User;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static Model model;
    private List<User> usersData;

    private Model() {
        this.usersData = new ArrayList<>();
    }

    public static Model getModelInstance() {
        if(model == null) {
            model = new Model();
        }

        return model;
    }

    public int getNumberOfUsers() {
        return usersData.size();
    }
}
