package com.example.locationtrackerdemo.data.local;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.locationtrackerdemo.data.DataSource;
import com.example.locationtrackerdemo.mvp.beans.GeoLocation;
import com.example.locationtrackerdemo.mvp.providers.GeoLocationsContentProvider;

import java.util.ArrayList;

public class LocalDataSource implements DataSource {
    private static LocalDataSource INSTANCE;

    private Context mContext;

    public LocalDataSource(Context context) {
        mContext = context;
    }

    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getAllLocation(GeAllLocationCallback callback) {

        Cursor cursor = null;

        ArrayList<GeoLocation> listGeoLocation = new ArrayList<>();

        try {
            cursor = mContext.getContentResolver().query(GeoLocationsContentProvider.CONTENT_URI, null, null, null, null);

            while (cursor != null && cursor.moveToNext()) {
                GeoLocation location = new GeoLocation(
                        cursor.getDouble(cursor.getColumnIndex(GeoLocationsContentProvider.KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(GeoLocationsContentProvider.KEY_LONGITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(GeoLocationsContentProvider.KEY_ACCURACY)));

                listGeoLocation.add(location);
            }

        } catch (Exception e) {
            e.printStackTrace();
            callback.failed(0, "No Location Found");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            callback.onSuccess(listGeoLocation);
        }
    }

    @Override
    public void deleteAllLocationFromDB(DeleteAllLocationCallBack callBack) {

        try {

            int count = mContext.getContentResolver().delete(GeoLocationsContentProvider.CONTENT_URI, null, null);
            Log.v("LocationDataSource", "deleted " + count + " rows");
            callBack.onSuccess();

        } catch (Exception e) {
            e.printStackTrace();
            callBack.failed(0, "Error While Deletion");
        }
    }

    @Override
    public void saveLocation(GeoLocation beanLocation) {

    }
}
