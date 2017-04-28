package com.example.locationtrackerdemo.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;


/**
 * Created by mithilesh on 8/20/16.
 */
public class NetworkUtils {

    public static final String TAG = NetworkUtils.class.getSimpleName();


    public static boolean isGPSEnabled(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void showGpsAlertIfDisabled(Context context) {

        if (!isGPSEnabled(context)) {
            showGPSDisabledAlertToUser(context);
        }
    }

    private static void showGPSDisabledAlertToUser(final Context context) {
        try {
            Log.v(TAG, "Showing no Gps Alert");
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("GPS Disabled")
                    .setMessage("Please Enable GPS")
                    .setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        context.startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                    .setCancelable(false)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
