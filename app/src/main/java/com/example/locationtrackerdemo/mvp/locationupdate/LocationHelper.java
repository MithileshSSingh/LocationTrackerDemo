package com.example.locationtrackerdemo.mvp.locationupdate;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context context;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private OnLocationReceived mLocationReceived;


    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = 3000;

    public interface OnLocationReceived {
        void onLocationReceived(Location location);
    }

    public LocationHelper(Context context) {
        this.context = context;

        buildGoogleApiClient();
        createLocationRequest();
    }


    public LocationRequest getmLocationRequest() {
        return mLocationRequest;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void createLocationRequest() {

        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        mLocationRequest.setSmallestDisplacement(10);
    }

    public void onStart(OnLocationReceived mLocationReceived) {
        this.mLocationReceived = mLocationReceived;
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            stopLocationUpdate();
            mGoogleApiClient.disconnect();
        }
    }

    private void stopLocationUpdate() {
        LocationServices
                .FusedLocationApi
                .removeLocationUpdates(
                        mGoogleApiClient,
                        this
                );
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLocationReceived != null) {
            if (location.getLatitude() > 0 && location.getLongitude() > 0) {
                mLocationReceived.onLocationReceived(location);
            }
        } else {
            Log.v("LocationHelper", "Location listener is null");
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        startLocationUpdates();
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices
                .FusedLocationApi
                .requestLocationUpdates(
                        mGoogleApiClient,
                        mLocationRequest,
                        this
                );
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("Location Helper", "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

}
