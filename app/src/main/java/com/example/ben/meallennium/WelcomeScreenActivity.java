package com.example.ben.meallennium;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class WelcomeScreenActivity extends Activity implements WelcomeScreenFragment.WelcomeScreenFragmentListener,
        RegisterFragment.RegisterFragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        Log.d("buildTest", "inside onCreate() of WelcomeScreenActivity");
        WelcomeScreenFragment welcomeScreenFragment = new WelcomeScreenFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_welcome_screen_container, welcomeScreenFragment);
        transaction.commit();
    }

    @Override
    public void onRegisterClick() {
        RegisterFragment registerScreenFragment = new RegisterFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_welcome_screen_container, registerScreenFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLoginClick() {

    }

    @Override
    public void onFragmentInteraction(){}
}
