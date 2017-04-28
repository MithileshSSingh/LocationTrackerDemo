package com.example.locationtrackerdemo.data.fake;

import android.content.Context;

import com.example.locationtrackerdemo.data.DataSource;
import com.example.locationtrackerdemo.mvp.beans.GeoLocation;


public class FakeDataSource implements DataSource {
    private static FakeDataSource INSTANCE;

    private Context mContext;

    public FakeDataSource(Context context) {
        mContext = context;
    }

    public static FakeDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FakeDataSource(context);
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

    private static class Error {
        private static final int ERROR_UNKNOWN = 0;
    }


}
