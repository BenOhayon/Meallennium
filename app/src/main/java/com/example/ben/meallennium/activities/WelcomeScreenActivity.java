package com.example.ben.meallennium.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.fragments.LoginFragment;
import com.example.ben.meallennium.fragments.RegistrationFragment;
import com.example.ben.meallennium.fragments.WelcomeScreenFragment;
import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.User;
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
        }

        WelcomeScreenFragment welcomeScreenFragment = new WelcomeScreenFragment();
        FragmentTransactions.createAndDisplayFragment(this, R.id.fragment_welcome_screen_container, welcomeScreenFragment, false);
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
        Model.instance.addUser(user, (User user1) -> {
            if(user1 != null) {
                Model.instance.setSignedInUserInFirebase(user1);

                SharedPreferences.Editor sharedEditor = getSharedPreferences("SP", MODE_PRIVATE).edit();
                sharedEditor.putString("userName", user1.getUsername());
                sharedEditor.apply();

                ProgressBarManager.dismissProgressBar();
                ToastMessageDisplayer.displayToast(WelcomeScreenActivity.this, user1.getUsername() + " signed up!");
                moveToPostListActivity();
            } else {
                ProgressBarManager.dismissProgressBar();
                ToastMessageDisplayer.displayToast(WelcomeScreenActivity.this, "Failed to sign up user");
            }
        });
    }

    @Override
    public void onLogin(User user) {
        Model.instance.signInUserToFirebase(user, (User user1) -> {
            if(user1 != null) {
                Model.instance.setSignedInUserInFirebase(user1);

                SharedPreferences.Editor sharedEditor = getSharedPreferences("SP", MODE_PRIVATE).edit();
                sharedEditor.putString("userName", user1.getUsername());
                sharedEditor.apply();

                ProgressBarManager.dismissProgressBar();
                ToastMessageDisplayer.displayToast(WelcomeScreenActivity.this, user1.getUsername() + " has signed in!");
                moveToPostListActivity();
            } else {
                ProgressBarManager.dismissProgressBar();
                ToastMessageDisplayer.displayToast(WelcomeScreenActivity.this, "Failed to sign in user");
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
