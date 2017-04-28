package com.example.locationtrackerdemo.mvp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;

/**
 * Created by mithilesh on 8/25/16.
 */
public class ActivityUtils {
    public static final String TAG = ActivityUtils.class.getSimpleName();

    public static ProgressBar mProgressBar;

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId,
                                             String fragmentTag, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(fragmentTag);
        }

        transaction.replace(frameId, fragment, fragmentTag);
        transaction.commit();
    }

    public static void addFragment(
            @NonNull final FragmentManager fragmentManager,
            @NonNull final Fragment fragment,
            final int frameId,
            final boolean addToBackStack,
            final String tag,
            final boolean isAnimate,
            final int animId) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction ft = fragmentManager.beginTransaction();

                if (isAnimate) {
                    ft.setCustomAnimations(animId,
                            animId, animId,
                            animId);
                }

                if (addToBackStack) {
                    ft.addToBackStack(tag);
                }

                ft.replace(frameId, fragment, tag);
                ft.commit();

            }
        });
    }

    public static void openActivity(Context baseContext, Class targetClass) {
        Intent intent = new Intent(baseContext, targetClass);
        baseContext.startActivity(intent);
    }

    public static void clearAllFromBackStack(FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
    }

    public static void clearAllExceptHomeBackStack(FragmentManager fm) {
        for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
            fm.popBackStack();
        }
    }

    public interface OnDialogOptionClickedCallBack {
        void positive();

        void negative();
    }

    public static void showAlertDialog(

            Context context,
            String title,
            String message,
            String positiveBtnText,
            String negativeBtnText,
            final OnDialogOptionClickedCallBack callBack) {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (callBack != null) {
                            callBack.positive();
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (callBack != null) {
                            callBack.negative();
                        }
                        dialog.dismiss();
                    }
                })
                .show();

    }
}
