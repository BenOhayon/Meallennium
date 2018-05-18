package com.example.ben.meallennium.model;

import com.example.ben.meallennium.activities.MeallenniumApplicationActivity;
import com.example.ben.meallennium.model.entities.Dinner;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.model.sql.DinnerSql;
import com.example.ben.meallennium.model.sql.SqlModel;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private static Model model;
    private List<User> usersData;
    private SqlModel sqlModel;

    private Model() {
        this.usersData = new ArrayList<>();
        sqlModel = new SqlModel(MeallenniumApplicationActivity.context);
    }

    public static Model getModelInstance() {
        if(model == null) {
            model = new Model();
        }

        return model;
    }

    public void addUserToLocalDatabase(User user) {
        sqlModel.addUserEntry(user);
    }

    public int getNumberOfUsers() {
        return usersData.size();
    }

    public void popUpAllUsers() {
        sqlModel.popUpAllUserEntries();
    }
}
