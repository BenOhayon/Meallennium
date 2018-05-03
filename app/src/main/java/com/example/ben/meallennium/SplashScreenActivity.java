package com.example.ben.meallennium;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(() -> {
            try {

                Thread.sleep(3500);
                Intent toMainScreen = new Intent(this, MainActivity.class);
                startActivity(toMainScreen);
                finish();

            } catch(InterruptedException e) {

            }
        }).start();
    }
}
