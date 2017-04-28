package com.example.locationtrackerdemo.mvp.providers;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.locationtrackerdemo.mvp.beans.GeoLocation;

/**
 * Created by lokesh on 15/10/15.
 */
public class GeoLocationsContentProvider extends BaseContentProvider {
    public static final String AUTHORITY = "com.example.locationtrackerdemo.mvp.provider.Geolocation";

    /**
     * A content:// style uri to the authority for the requests provider
     */
    public static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/geolocations");

    public static final String TABLE_NAME_GEOLOCATIONS = "Geolocations";

    public static final String KEY_ID = "_id";
    public static final String KEY_ACCURACY = "accuracy";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    @Override
    public boolean onCreate() {
        CONTENT_NAME = AUTHORITY;
        CONTENT_TYPE = "geolocations";
        TABLE_NAME = TABLE_NAME_GEOLOCATIONS;
        PRIMARY_KEY = KEY_ID;

        return super.onCreate();
    }

    public static ContentValues locationToContentValues(GeoLocation geoLocation) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, geoLocation.getId());
        values.put(KEY_LATITUDE, geoLocation.getLatitude());
        values.put(KEY_LONGITUDE, geoLocation.getLongitude());
        values.put(KEY_ACCURACY, geoLocation.getAccuracy());

        return values;
    }

    public static GeoLocation convertToGeolocationModel(Cursor cursor) {
        GeoLocation geoLocation = new GeoLocation();

        geoLocation.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        geoLocation.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)));
        geoLocation.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)));
        geoLocation.setAccuracy(cursor.getDouble(cursor.getColumnIndex(KEY_ACCURACY)));

        return geoLocation;
    }
}
