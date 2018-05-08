package com.example.ben.meallennium.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public final class FragmentTransactions {

    /**
     * Creates a fragment transaction to the parent activity and displays it.
     *
     * @param context The context in which <code>fragmentToDisplay</code> will be displayed.
     * @param containerViewId The container id of the container in which the fragment will be displayed.
     * @param fragmentToDisplay The fragment to display in the selected container.
     * @param addToBackStack If you request to push this transaction to the back stack, then this option will be
     *                       <code>true</code>, otherwise will be <code>false</code>.
     */
    public static void createAndDisplayFragment(AppCompatActivity context, int containerViewId, Fragment fragmentToDisplay, boolean addToBackStack) {
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragmentToDisplay);

        if(addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
