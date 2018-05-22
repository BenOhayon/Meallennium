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
import com.example.ben.meallennium.model.entities.Restaurant;
import com.example.ben.meallennium.utils.FieldVerifier;
import com.example.ben.meallennium.utils.RegisterControllerListener;

public class RestaurantRegistrationFragment extends Fragment {

    private RegisterControllerListener listener;

    public RestaurantRegistrationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_registration, container, false);

        Button registerButton = view.findViewById(R.id.restaurantRegisterScreen__RegisterButton);
        Button cancelButton = view.findViewById(R.id.restaurantRegisterScreen__cancelButton);

        EditText restaurantNameEt = view.findViewById(R.id.restaurantRegisterScreen__restaurantNameEt);
        EditText restaurantOwnerEt = view.findViewById(R.id.restaurantRegisterScreen__restaurantOwnerEt);
        EditText restaurantPasswordEt = view.findViewById(R.id.restaurantRegisterScreen__restaurantPasswordEt);
        EditText restaurantVerifyPasswordEt = view.findViewById(R.id.restaurantRegisterScreen__restaurantPasswordValidationEt);
        EditText restaurantEmailEt = view.findViewById(R.id.restaurantRegisterScreen__restaurantEmailEt);

        registerButton.setOnClickListener((View v) -> {
            if(listener != null) {
                String name = restaurantNameEt.getText().toString();
                String owner = restaurantOwnerEt.getText().toString();
                String password = restaurantPasswordEt.getText().toString();
                String email = restaurantEmailEt.getText().toString();
                String verifiedPassword = restaurantVerifyPasswordEt.getText().toString();

                if(FieldVerifier.areVerifiedFields(email, password, verifiedPassword)) {
                    Restaurant restaurant = new Restaurant(name, owner, email, password);
                    listener.onRegister(restaurant);
                } else {
                    listener.onError();
                }

                restaurantNameEt.setText("");
                restaurantOwnerEt.setText("");
                restaurantPasswordEt.setText("");
                restaurantEmailEt.setText("");
                restaurantVerifyPasswordEt.setText("");
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
