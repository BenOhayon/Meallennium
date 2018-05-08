package com.example.ben.meallennium.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.fragments.DinnerRegistrationFragment;
import com.example.ben.meallennium.fragments.RegistrationFragment;
import com.example.ben.meallennium.fragments.RestaurantRegistrationFragment;
import com.example.ben.meallennium.fragments.WelcomeScreenFragment;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.LoginControllerListener;
import com.example.ben.meallennium.utils.RegisterControllerListener;

public class WelcomeScreenActivity extends AppCompatActivity implements
        WelcomeScreenFragment.WelcomeScreenFragmentListener,
        RegistrationFragment.RegisterFragmentListener,
        RegisterControllerListener,
        LoginControllerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        WelcomeScreenFragment welcomeScreenFragment = new WelcomeScreenFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_welcome_screen_container, welcomeScreenFragment, false);
    }

    @Override
    public void onRegisterOptionSelect() {
        RegistrationFragment registerScreenFragment = new RegistrationFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_welcome_screen_container, registerScreenFragment, true);
    }

    @Override
    public void onLoginOptionSelect() {

    }

    @Override
    public void onRestaurantRegisterOptionSelect() {
        RestaurantRegistrationFragment restaurantRegistrationFragment = new RestaurantRegistrationFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_welcome_screen_container, restaurantRegistrationFragment, true);
    }

    @Override
    public void onDinnerRegisterOptionSelect() {
        DinnerRegistrationFragment dinnerRegistrationFragment = new DinnerRegistrationFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_welcome_screen_container, dinnerRegistrationFragment, true);
    }

    @Override
    public void onRegister() {
        // TODO append the account saving functionality in time.
        Intent toPostListActivity = new Intent(this, PostsListActivity.class);
        startActivity(toPostListActivity);
        finish();
    }

    @Override
    public void onLogin() {
        // TODO append the account authentication in time.

    }

    @Override
    public void onCancel() {
        getSupportFragmentManager().popBackStack();
    }
}
