package com.example.locationtrackerdemo.data.remote;

import android.content.Context;

import com.example.locationtrackerdemo.data.DataSource;
import com.example.locationtrackerdemo.mvp.beans.GeoLocation;


public class RemoteDataSource implements DataSource {


    private static RemoteDataSource INSTANCE = null;

    private Context mContext;

    private RemoteDataSource() {

    }

    private RemoteDataSource(Context context) {
        mContext = context;
    }

    public static RemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(context);
        }

        return INSTANCE;
    }

    @Override
    public void getAllLocation(GeAllLocationCallback callback) {

    }

    @Override
    public void deleteAllLocationFromDB(DeleteAllLocationCallBack callBack) {

    }

    @Override
    public void saveLocation(GeoLocation beanLocation) {

    }
}
