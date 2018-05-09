package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ben.meallennium.R;

public class RegistrationFragment extends Fragment {

    public interface RegisterFragmentListener {
        void onRestaurantRegisterOptionSelect();
        void onDinnerRegisterOptionSelect();
    }

    private RegisterFragmentListener listener;

    public RegistrationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        Button restaurantRegister = view.findViewById(R.id.registrationScreen__restaurantRegister);
        Button dinnerRegister = view.findViewById(R.id.registrationScreen__dinnerRegister);

        restaurantRegister.setOnClickListener((View v) -> {
            if(listener != null) {
                listener.onRestaurantRegisterOptionSelect();
            }
        });

        dinnerRegister.setOnClickListener((View v) -> {
            if(listener != null) {
                listener.onDinnerRegisterOptionSelect();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragmentListener) {
            listener = (RegisterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
