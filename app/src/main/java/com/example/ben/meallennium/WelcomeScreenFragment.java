package com.example.ben.meallennium;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WelcomeScreenFragment extends Fragment {

    public interface WelcomeScreenFragmentListener {
        void onRegisterClick();
        void onLoginClick();
    }

    private WelcomeScreenFragmentListener listener;

    public WelcomeScreenFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);

        Button registerButton = view.findViewById(R.id.welcomeScreenFragment__registrationButton);
        Button loginButton = view.findViewById(R.id.welcomeScreenFragment__loginButton);

        registerButton.setOnClickListener((View v) -> {
            if(listener != null) {
                Log.d("buildTest", "onRegisterClick() was invoked!");
                listener.onRegisterClick();
            } else {
                Log.d("buildTest", "listener is null");
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("buildTest", "in onAttach() of WelcomeScreenFragment.");

        if(context instanceof WelcomeScreenFragmentListener) {
            listener = (WelcomeScreenFragmentListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement WelcomeScreenFragmentListener interface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
