package com.example.ben.meallennium.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ben.meallennium.R;

public class AboutFragment extends Fragment {

    public interface AboutFragmentListener {
        void onOKPressed();
    }

    private AboutFragmentListener listener;

    public AboutFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        Button ok = view.findViewById(R.id.aboutScreen__OKButton);
        ok.setOnClickListener((View v) -> {
            listener.onOKPressed();
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AboutFragmentListener) {
            listener = (AboutFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AboutFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
