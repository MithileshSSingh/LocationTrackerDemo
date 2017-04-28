package com.example.locationtrackerdemo.mvp.customviews;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by mithilesh on 9/6/16.
 */
public class CustomProgressBar {
    private static String TAG = CustomProgressBar.class.getSimpleName();

    private static CustomProgressBar INSTANCE;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    private CustomProgressBar() {

    }

    private CustomProgressBar(Context context) {
        mContext = context;
        mProgressDialog = new ProgressDialog(context);
    }

    public static CustomProgressBar getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CustomProgressBar(context);
        }
        return INSTANCE;
    }

    public void show(String title, String message) {
        if (mProgressDialog != null) {
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public void show(String message) {

        if (mProgressDialog != null) {
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public void hide() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
            INSTANCE = null;
        }
    }
}
