package com.example.locationtrackerdemo.mvp.screen_home;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.locationtrackerdemo.R;
import com.example.locationtrackerdemo.di.RepositoryInjector;
import com.example.locationtrackerdemo.mvp.BaseFragment;
import com.example.locationtrackerdemo.mvp.beans.GeoLocation;
import com.example.locationtrackerdemo.mvp.customviews.CustomStageButton;
import com.example.locationtrackerdemo.mvp.locationupdate.LocationUpdateService;
import com.example.locationtrackerdemo.util.AppConstants;
import com.example.locationtrackerdemo.util.AppPref;
import com.example.locationtrackerdemo.util.NetworkUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mithilesh on 9/11/16.
 */
public class HomeFragment extends BaseFragment implements HomeContract.View, HomeContract.OnItemSelectedListener, View.OnClickListener, CustomStageButton.OnStageChangedListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    public static final String TAG = HomeFragment.class.getSimpleName();

    private HomeContract.Presenter mPresenter;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new HomePresenter(
                RepositoryInjector.provideRepository(mActivity.getApplicationContext()),
                this
        );

        if (!isGooglePlayServicesAvailable(mActivity))
            return;

        init();
    }

    private CustomStageButton btnStartTracking;
    private CustomStageButton btnStopTracking;

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mMapFragment;

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;

    private static Intent locationServiceIntent;
    private static PendingIntent locationServicePendingIntent;

    @Override
    protected void init() {
        initView();
        initGoogleThings();
        initMembers();
        initListeners();
        initData();
    }


    @Override
    protected void initView() {
        btnStartTracking = (CustomStageButton) mView.findViewById(R.id.btnStartTracking);
        btnStopTracking = (CustomStageButton) mView.findViewById(R.id.btnStopTracking);
    }

    @Override
    protected void initMembers() {
        if (AppPref.getInstance(mActivity).getBoolean(AppPref.Keys.IS_TRACKING)) {
            startLocationTracking(true);
        } else {
            startLocationTracking(false);
        }
    }

    @Override
    protected void initListeners() {
        btnStartTracking.setOnStageChangedListener(this);
        btnStopTracking.setOnStageChangedListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(int position) {

    }

    private void initGoogleThings() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            initPermission();
        } else {
            initGoogleAPIClient();
        }
    }

    private void initGoogleAPIClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(mActivity)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(LocationServices.API)
                    .enableAutoManage(mActivity, this)
                    .addConnectionCallbacks(this)
                    .build();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initGoogleMap();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void initGoogleMap() {
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mMapFragment != null) {
            mMapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(mActivity);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Checking For Google Play Service that is Mandatory for Google Map
     */
    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    private void startLocationTracking(boolean isStartTracking) {

        if (isStartTracking) {
            btnStartTracking.setVisibility(View.GONE);
            btnStopTracking.setVisibility(View.VISIBLE);
            btnStopTracking.enabled(true);
            startLocationUpdateService();
        } else {
            btnStartTracking.setVisibility(View.VISIBLE);
            btnStopTracking.setVisibility(View.GONE);
            btnStartTracking.enabled(true);
            loadAllLocationFromDB();
            stopLocationUpdateService();
        }
    }


    private void deleteAllLocationFromDB() {
        mPresenter.deleteAllLocationFromDB(new DeleteAllLocationCallBack() {
            @Override
            public void onSuccess() {
                /**
                 * DB Cleared For new Tracking Detail
                 */
            }

            @Override
            public void failed(int errorCode, String msgError) {
                /**
                 * Error While Clearing DB
                 */
            }
        });
    }

    private void loadAllLocationFromDB() {
        mPresenter.getAllLocation(new GeAllLocationCallback() {
            @Override
            public void onSuccess(ArrayList<GeoLocation> data) {
                /**
                 * Location Loaded from DB
                 * NOW
                 * Draw Polyline
                 */

                if (data != null && data.size() > 0) {
                    List<LatLng> listLatLng = new ArrayList<LatLng>();
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    for (GeoLocation geoLocation : data) {
                        LatLng latLng = new LatLng(geoLocation.getLatitude(), geoLocation.getLongitude());

                        listLatLng.add(latLng);
                        builder.include(latLng);
                    }

                    if (mGoogleMap != null) {
                        PolylineOptions green = new PolylineOptions().color(Color.GREEN).clickable(true).addAll(listLatLng).geodesic(true);
                        PolylineOptions red = new PolylineOptions().color(Color.RED).clickable(true).addAll(listLatLng);

                        mGoogleMap.addPolyline(green);
                        mGoogleMap.addPolyline(red);

                        LatLngBounds latLngBounds = builder.build();
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 18));
                    }
                }
            }

            @Override
            public void failed(int errorCode, String msgError) {
                Toast.makeText(mActivity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This will Get Called after button Swiped
     *
     * @param direction : Define Direction of Swipe : LEFT-RIGHT(0) OR RIGHT-LEFT(1)
     */
    @Override
    public void onStateChanged(int direction) {

        switch (direction) {
            case CustomStageButton.SWIPE_RIGHT:
                deleteAllLocationFromDB();
                AppPref.getInstance(mActivity).putBoolean(AppPref.Keys.IS_TRACKING, true);
                startLocationTracking(true);
                break;
            case CustomStageButton.SWIPE_LEFT:
                AppPref.getInstance(mActivity).putBoolean(AppPref.Keys.IS_TRACKING, false);
                startLocationTracking(false);
                break;
        }

    }


    public void startLocationUpdateService() {
        Log.v("LocationUpdate", "starting location update service");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        if (locationServiceIntent == null) {
            locationServiceIntent = new Intent(mActivity, LocationUpdateService.class);
        }

        AlarmManager alarm = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
        if (locationServicePendingIntent == null) {
            locationServicePendingIntent = PendingIntent.getService(mActivity, 0,
                    locationServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        alarm.cancel(locationServicePendingIntent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                AppConstants.Time.LOCATION_UPDATE_ALARM_INTERVAL, locationServicePendingIntent);

    }

    public void stopLocationUpdateService() {
        Log.v("LocationUpdate", "stopping location update service");
        if (locationServicePendingIntent != null) {
            AlarmManager alarm = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(locationServicePendingIntent);
        }

        if (locationServiceIntent != null) {
            boolean serviceStopped = mActivity.stopService(locationServiceIntent);
            Log.v("LocationUpdate", "LocationUpdate stopped= " + serviceStopped);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v(TAG, "MAP is Ready");
        mGoogleMap = googleMap;

        updateLocationUI();
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (mGoogleMap == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationPermissionGranted = true;

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                NetworkUtils.showGpsAlertIfDisabled(mActivity);

                return false;
            }
        });

    }

    private void getDeviceLocation() {
        if (mGoogleMap == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationPermissionGranted = true;
        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastKnownLocation != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), 15));
        }
    }

    private void initPermission() {
        Dexter.withActivity(mActivity)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (!report.areAllPermissionsGranted()) {
                    mActivity.finish();
                    mLocationPermissionGranted = false;
                } else {
                    mLocationPermissionGranted = true;
                    initGoogleAPIClient();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

}
