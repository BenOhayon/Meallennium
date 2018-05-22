package com.example.ben.meallennium.utils;

import com.example.ben.meallennium.model.entities.User;

public interface LoginControllerListener {
    void onLogin(User user);
    void onCancel();
    void onError();
}
