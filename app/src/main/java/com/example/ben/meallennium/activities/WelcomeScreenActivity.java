package com.example.ben.meallennium.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.fragments.LoginFragment;
import com.example.ben.meallennium.fragments.RegistrationFragment;
import com.example.ben.meallennium.fragments.WelcomeScreenFragment;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.model.firebase.FirebaseModel;
import com.example.ben.meallennium.utils.FragmentTransactions;
import com.example.ben.meallennium.utils.LoginControllerListener;
import com.example.ben.meallennium.utils.ProgressBarManager;
import com.example.ben.meallennium.utils.RegisterControllerListener;
import com.example.ben.meallennium.utils.ToastMessageDisplayer;

public class WelcomeScreenActivity extends AppCompatActivity implements
        WelcomeScreenFragment.WelcomeScreenFragmentListener,
        RegisterControllerListener,
        LoginControllerListener {

    // --------------------
    //   CALLBACK METHODS
    // --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        if(Model.instance.isSignedInUserInFirebase()) {
            Log.d("buildTest", "Moving to post list activity");
            moveToPostListActivity();
        } else {
            WelcomeScreenFragment welcomeScreenFragment = new WelcomeScreenFragment();
            FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_welcome_screen_container, welcomeScreenFragment, false);
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
    public void onRegister(User user) {
        Model.instance.addUserToFirebase(user, new FirebaseModel.OnCreateNewUserListener() {
            @Override
            public void onCreationComplete(User user) {
                if(user != null) {
                    Model.instance.setSignedInUserInFirebase(user);
                    ProgressBarManager.dismissProgressBar();
                    ToastMessageDisplayer.displayToast(WelcomeScreenActivity.this, "The user " + user + " signed up!");
                    moveToPostListActivity();
                } else {
                    ProgressBarManager.dismissProgressBar();
                    ToastMessageDisplayer.displayToast(WelcomeScreenActivity.this, "Failed to sign up user");
                }
            }
        });
    }

    @Override
    public void onLogin(User user) {
        Model.instance.signInUserToFirebase(user, new FirebaseModel.OnSignInUserListener() {
            @Override
            public void onSignInComplete(User user) {
                if(user != null) {
                    Model.instance.setSignedInUserInFirebase(user);
                    ProgressBarManager.dismissProgressBar();
                    ToastMessageDisplayer.displayToast(WelcomeScreenActivity.this, "A user has signed in!");
                    moveToPostListActivity();
                } else {
                    ProgressBarManager.dismissProgressBar();
                    ToastMessageDisplayer.displayToast(WelcomeScreenActivity.this, "Failed to sign in user");
                }
            }
        });
    }

    @Override
    public void onCancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onError(String error) {
        ProgressBarManager.dismissProgressBar();
        ToastMessageDisplayer.displayToast(this, error);
    }


    // --------------------
    //   PRIVATE METHODS
    // --------------------
    private void moveToPostListActivity() {
        Intent toPostListActivity = new Intent(this, PostsListActivity.class);
        startActivity(toPostListActivity);
    }
}
