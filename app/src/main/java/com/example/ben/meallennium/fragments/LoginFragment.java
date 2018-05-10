package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.utils.LoginControllerListener;

public class LoginFragment extends Fragment {

    private LoginControllerListener listener;

    public LoginFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = view.findViewById(R.id.loginScreen__loginButton);
        Button cancelButton = view.findViewById(R.id.loginScreen__cancelButton);

        loginButton.setOnClickListener((View v) -> {
            listener.onLogin();
        });

        cancelButton.setOnClickListener((View v) -> {
            listener.onCancel();
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginControllerListener) {
            listener = (LoginControllerListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LoginControllerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
