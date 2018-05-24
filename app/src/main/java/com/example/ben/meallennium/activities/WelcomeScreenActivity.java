package com.example.ben.meallennium.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.fragments.DinnerRegistrationFragment;
import com.example.ben.meallennium.fragments.LoginFragment;
import com.example.ben.meallennium.fragments.RegistrationFragment;
import com.example.ben.meallennium.fragments.RestaurantRegistrationFragment;
import com.example.ben.meallennium.fragments.WelcomeScreenFragment;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.LoginControllerListener;
import com.example.ben.meallennium.utils.RegisterControllerListener;
import com.example.ben.meallennium.utils.ToastMessageDisplayer;

public class WelcomeScreenActivity extends AppCompatActivity implements
        WelcomeScreenFragment.WelcomeScreenFragmentListener,
        RegistrationFragment.RegisterFragmentListener,
        RegisterControllerListener,
        LoginControllerListener, FirebaseModel.FirebaseUserAuthListener {


    // --------------------
    //   CALLBACK METHODS
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        Model.instance.setListenerForFirebaseModel(this);
//        Model.instance.savePostsInFirebase();

        WelcomeScreenFragment welcomeScreenFragment = new WelcomeScreenFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_welcome_screen_container, welcomeScreenFragment, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(Model.instance.isSignedInUserInFirebase()) {
            moveToPostListActivity();
        }
    }

    // --------------------
    //   LISTENER METHODS
    // --------------------

    @Override
    public void onRegisterOptionSelect() {
        RegistrationFragment registerScreenFragment = new RegistrationFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_welcome_screen_container, registerScreenFragment, true);
    }

    @Override
    public void onLoginOptionSelect() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_welcome_screen_container, loginFragment, true);
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
    public void onRegister(User user) {
        Model.instance.addUserToFirebase(user);
    }

    @Override
    public void onLogin(User user) {
        Model.instance.signInUserToFirebase(user);
    }

    @Override
    public void onCancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCreateUserSuccess(User user) {
        Model.instance.setSignedInUserInFirebase(user);
        ToastMessageDisplayer.displayToast(this, "The user " + user + " signed up!");
        moveToPostListActivity();
    }

    @Override
    public void onCreateUserFailure(User user) {
        ToastMessageDisplayer.displayToast(this, "Failed to sign up user");
    }

    @Override
    public void onSignInUserSuccess(User user) {
        Model.instance.setSignedInUserInFirebase(user);
        ToastMessageDisplayer.displayToast(this, "A user has signed in!");
        moveToPostListActivity();
    }

    @Override
    public void onSignInUserFailure(User user) {
        ToastMessageDisplayer.displayToast(this, "Failed to sign in user");
    }

    @Override
    public void onError() {
        ToastMessageDisplayer.displayToast(this, "Invalid fields");
    }


    // --------------------
    //   PRIVATE METHODS
    // --------------------
    private void moveToPostListActivity() {
        Intent toPostListActivity = new Intent(this, PostsListActivity.class);
        startActivity(toPostListActivity);
    }
}
