package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ben.meallennium.R;
import com.example.ben.meallennium.utils.RegisterControllerListener;

public class DinnerRegistrationFragment extends Fragment {

    private RegisterControllerListener listener;

    public DinnerRegistrationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dinner_registration, container, false);

        Button registerButton = view.findViewById(R.id.dinnerRegisterScreen__registerButton);
        Button cancelButton = view.findViewById(R.id.dinnerRegisterScreen__cancelButton);

        registerButton.setOnClickListener((View v) -> {
            if(listener != null) {
                listener.onRegister();
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
