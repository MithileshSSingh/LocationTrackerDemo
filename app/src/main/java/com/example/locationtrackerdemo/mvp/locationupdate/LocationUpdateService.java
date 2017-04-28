package com.example.locationtrackerdemo.mvp.locationupdate;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.locationtrackerdemo.App;
import com.example.locationtrackerdemo.mvp.providers.GeoLocationsContentProvider;

import java.util.Date;

public class LocationUpdateService extends Service implements LocationHelper.OnLocationReceived {

    private final String TAG = getClass().getSimpleName();
    private LocationHelper locationHelper;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "Locationupdateservice.onStartCommand");

        locationHelper = App.getInstance().getLocationHelper(getApplicationContext());
        locationHelper.onStart(this);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "LocationUpdateService.oncreate called");
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "destroying LocationUpdateService");
        if (locationHelper != null) {
            locationHelper.onStop();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationReceived(Location newLocation) {
        Log.v(TAG, "Location Received" + newLocation.toString());

        Date date = new Date();


        insertLocationToDB(
                newLocation.getLatitude(),
                newLocation.getLongitude(),
                newLocation.getAccuracy());


    }

    /**
     * @param latitude  : Latitude to Location
     * @param longitude : Longitude of the Location
     * @param accuracy  : Accuracy
     */
    public void insertLocationToDB(double latitude,
                                   double longitude,
                                   double accuracy) {

        ContentValues values = new ContentValues();

        values.put(GeoLocationsContentProvider.KEY_LATITUDE, latitude);
        values.put(GeoLocationsContentProvider.KEY_LONGITUDE, longitude);
        values.put(GeoLocationsContentProvider.KEY_ACCURACY, accuracy);

        Uri insertedRow = getContentResolver().insert(GeoLocationsContentProvider.CONTENT_URI, values);

        Log.v("LocationUpdateService", "Location inserted in db. uri = " + insertedRow + ", contents=" + values);
    }
}
