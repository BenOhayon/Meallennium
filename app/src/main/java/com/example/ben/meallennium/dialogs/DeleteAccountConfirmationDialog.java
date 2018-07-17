package com.example.ben.meallennium.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.User;
import com.example.ben.meallennium.model.firebase.FirebaseModel;

public class DeleteAccountConfirmationDialog extends DialogFragment {

    public interface DeleteAccountConfirmationDialogListener {
        void onYesClickedOnDeleteAccountDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DeleteAccountConfirmationDialogListener listener =
                (DeleteAccountConfirmationDialogListener) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete the account?")
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Model.instance.deleteSignedInUserInFirebase(new FirebaseModel.OnUserDeleteListener() {
                            @Override
                            public void onDeletionComplete(User user) {
                                Model.instance.setSignedInUserInFirebase(null);
                                listener.onYesClickedOnDeleteAccountDialog();
                                dismiss();
                            }
                        });
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
