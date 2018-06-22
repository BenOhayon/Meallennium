package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ben.meallennium.R;

public class WelcomeScreenFragment extends Fragment {

    public interface WelcomeScreenFragmentListener {
        void onRegisterOptionSelect();
        void onLoginOptionSelect();
    }

    private WelcomeScreenFragmentListener listener;

    public WelcomeScreenFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);

        Button registerButton = view.findViewById(R.id.welcomeScreen__registrationButton);
        Button loginButton = view.findViewById(R.id.welcomeScreen__loginButton);

        registerButton.setOnClickListener((View v) -> {
            if(listener != null) {
                listener.onRegisterOptionSelect();
            }
        });

        loginButton.setOnClickListener((View v) -> {
            if(listener != null) {
                listener.onLoginOptionSelect();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof WelcomeScreenFragmentListener) {
            listener = (WelcomeScreenFragmentListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement WelcomeScreenFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
