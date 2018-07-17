package com.example.ben.meallennium.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.ben.meallennium.model.Model;

public class LogoutConfirmationDialog extends DialogFragment {

    public interface LogoutConfirmationDialogListener {
        void onYesClickedOnLogoutDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LogoutConfirmationDialogListener listener =
                (LogoutConfirmationDialogListener) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to logout?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Model.instance.signOutCurrentUserFromFirebase();
                        listener.onYesClickedOnLogoutDialog();
                    }
                })
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
