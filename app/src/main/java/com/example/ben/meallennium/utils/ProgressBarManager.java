package com.example.ben.meallennium.utils;

import android.view.View;
import android.widget.ProgressBar;

public class ProgressBarManager {

    private static ProgressBar loadingProgressBar;

    public static void bindProgressBar(ProgressBar progressBar) {
        loadingProgressBar = progressBar;
    }

    public static void showProgressBar() {
        if(loadingProgressBar != null) {
            loadingProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public static void dismissProgressBar() {
        if(loadingProgressBar != null) {
            loadingProgressBar.setVisibility(View.GONE);
        }
    }
}
