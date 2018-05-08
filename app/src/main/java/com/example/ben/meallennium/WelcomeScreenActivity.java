package com.example.ben.meallennium;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class WelcomeScreenActivity extends AppCompatActivity implements
        WelcomeScreenFragment.WelcomeScreenFragmentListener,
        RegistrationFragment.RegisterFragmentListener,
        RestaurantRegistrationFragment.RestaurantRegistrationFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        WelcomeScreenFragment welcomeScreenFragment = new WelcomeScreenFragment();
        createAndDisplayFragment(R.id.fragment_welcome_screen_container, welcomeScreenFragment, false);
    }

    @Override
    public void onRegisterClick() {
        RegistrationFragment registerScreenFragment = new RegistrationFragment();
        createAndDisplayFragment(R.id.fragment_welcome_screen_container, registerScreenFragment, true);
    }

    @Override
    public void onLoginClick() {

    }


    @Override
    public void onRestaurantRegisterClick() {
        RestaurantRegistrationFragment restaurantRegistrationFragment = new RestaurantRegistrationFragment();
        createAndDisplayFragment(R.id.fragment_welcome_screen_container, restaurantRegistrationFragment, true);
    }

    @Override
    public void onDinnerRegisterClick() {

    }

    /**
     * Creates a fragment transaction to the parent activity and displays it.
     *
     * @param containerViewId The container id of the container in which the fragment will be displayed.
     * @param fragmentToDisplay The fragment to display in the selected container.
     * @param addToBackStack If you request to push this transaction to the back stack, then this option will be
     *                       <code>true</code>, otherwise will be <code>false</code>.
     */
    private void createAndDisplayFragment(int containerViewId, Fragment fragmentToDisplay, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragmentToDisplay);

        if(addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
