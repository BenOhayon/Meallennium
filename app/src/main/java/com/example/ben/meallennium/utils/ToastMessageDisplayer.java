package com.example.ben.meallennium.utils;

import android.content.Context;
import android.widget.Toast;

public final class ToastMessageDisplayer {

    public static void displayToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

}
