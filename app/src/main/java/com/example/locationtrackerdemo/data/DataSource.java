package com.example.locationtrackerdemo.data;

import com.example.locationtrackerdemo.mvp.beans.GeoLocation;

import java.util.ArrayList;

/**
 * Created by mithilesh on 8/23/16.
 */
public interface DataSource {
    interface GeAllLocationCallback {
        void onSuccess(ArrayList<GeoLocation> data);

        void failed(int errorCode, String msgError);
    }

    void getAllLocation(GeAllLocationCallback callback);


    interface DeleteAllLocationCallBack {
        void onSuccess();

        void failed(int errorCode, String msgError);
    }

    void deleteAllLocationFromDB(DeleteAllLocationCallBack callBack);

    void saveLocation(GeoLocation beanLocation);
}
