package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.utils.FieldVerifier;
import com.example.ben.meallennium.utils.ProgressBarManager;
import com.example.ben.meallennium.utils.RegisterControllerListener;

public class RegistrationFragment extends Fragment {

    private RegisterControllerListener listener;

    public RegistrationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        Button registerButton = view.findViewById(R.id.registerScreen__registerButton);
        Button cancelButton = view.findViewById(R.id.registerScreen__cancelButton);

        EditText emailEt = view.findViewById(R.id.registerScreen__emailEt);
        EditText passwordEt = view.findViewById(R.id.registerScreen__passwordEt);
        EditText verifyPasswordEt = view.findViewById(R.id.registerScreen__verifyPasswordEt);

        ProgressBar loadingProgressBar = view.findViewById(R.id.dinnerRegisterScreen__progressBar);
        ProgressBarManager.bindProgressBar(loadingProgressBar);

        registerButton.setOnClickListener((View v) -> {
            if(listener != null) {
                ProgressBarManager.showProgressBar();

                String email = emailEt.getText().toString();
                String pass = passwordEt.getText().toString();
                String verifiedPass = verifyPasswordEt.getText().toString();

                if(FieldVerifier.verifyRegistration(email, pass, verifiedPass)) {
                    User user = new User(email, pass);
                    listener.onRegister(user);
                } else {
                    listener.onError("Invalid email or password");
                }

                emailEt.setText("");
                passwordEt.setText("");
                verifyPasswordEt.setText("");
            }
        });

        cancelButton.setOnClickListener((View v) -> {
            if(listener != null) {
                listener.onCancel();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterControllerListener) {
            listener = (RegisterControllerListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterControllerListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
