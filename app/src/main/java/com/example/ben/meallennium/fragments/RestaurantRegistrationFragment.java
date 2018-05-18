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

        registerButton.setOnClickListener((View v) -> {
            if(listener != null) {
                String name = restaurantNameEt.getText().toString();
                String owner = restaurantOwnerEt.getText().toString();
                String password = restaurantPasswordEt.getText().toString();
                Restaurant restaurant = new Restaurant(name, owner, password);
                listener.onRegister(restaurant);
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
