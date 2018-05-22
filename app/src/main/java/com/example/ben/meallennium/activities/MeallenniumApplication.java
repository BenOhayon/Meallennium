package com.example.ben.meallennium.activities;

import android.app.Application;
import android.content.Context;

public class MeallenniumApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
