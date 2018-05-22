package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.model.entities.Dinner;
import com.example.ben.meallennium.utils.FieldVerifier;
import com.example.ben.meallennium.utils.RegisterControllerListener;
import com.example.ben.meallennium.utils.ToastMessageDisplayer;

public class DinnerRegistrationFragment extends Fragment {

    private RegisterControllerListener listener;

    public DinnerRegistrationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dinner_registration, container, false);

        Button registerButton = view.findViewById(R.id.dinnerRegisterScreen__registerButton);
        Button cancelButton = view.findViewById(R.id.dinnerRegisterScreen__cancelButton);

        EditText emailEt = view.findViewById(R.id.dinnerRegisterScreen__emailEt);
        EditText passwordEt = view.findViewById(R.id.dinnerRegisterScreen__passwordEt);
        EditText verifyPasswordEt = view.findViewById(R.id.dinnerRegisterScreen__verifyPasswordEt);

        registerButton.setOnClickListener((View v) -> {
            if(listener != null) {
                String email = emailEt.getText().toString();
                String pass = passwordEt.getText().toString();
                String verifiedPass = verifyPasswordEt.getText().toString();

                if(FieldVerifier.areVerifiedFields(email, pass, verifiedPass)) {
                    Dinner dinner = new Dinner(email, pass);
                    listener.onRegister(dinner);
                } else {
                    listener.onError();
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
