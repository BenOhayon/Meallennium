package com.example.ben.meallennium;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(() -> {
            try {

                Thread.sleep(3500);
                Intent toMainScreen = new Intent(this, WelcomeScreenActivity.class);
                startActivity(toMainScreen);
                finish();

            } catch(InterruptedException e) {

            }
        }).start();
    }
}
