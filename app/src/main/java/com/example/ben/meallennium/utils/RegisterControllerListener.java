package com.example.ben.meallennium.utils;

import com.example.ben.meallennium.model.entities.User;

public interface RegisterControllerListener {
    void onRegister(User user);
    void onCancel();
    void onError(String error);
}
