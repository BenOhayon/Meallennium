package com.example.ben.meallennium.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.ben.meallennium.model.Model;
import com.example.ben.meallennium.model.entities.Post;
import com.example.ben.meallennium.utils.ProgressBarManager;

public class DeletePostConfirmDialog extends DialogFragment {

    public interface DeletePostConfirmDialogListener {
        void onYesClickedOnDeletePostDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Post postToDelete = (Post) getArguments().getSerializable("PostToDelete");
        DeletePostConfirmDialogListener listener =
                (DeletePostConfirmDialogListener) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this post?");
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProgressBarManager.showProgressBar();
                Model.instance.deletePost(postToDelete, new Model.OnOperationCompleteListener() {
                    @Override
                    public void onComplete() {
                        listener.onYesClickedOnDeletePostDialog();
                    }
                });
            }
        }).setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProgressBarManager.dismissProgressBar();
            }
        });

        return builder.create();
    }
}
