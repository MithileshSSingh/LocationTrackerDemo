package com.example.locationtrackerdemo.data;

import android.content.Context;

import com.example.locationtrackerdemo.mvp.beans.GeoLocation;

import java.util.ArrayList;


/**
 * Created by mithilesh on 8/23/16.
 */
public class Repository implements DataSource {
    private static Repository INSTANCE = null;

    private DataSource mLocalDataSource = null;
    private DataSource mRemoteDataSource = null;
    private DataSource mFakeDataSource = null;

    private Context mContext;

    private Repository() {

    }

    private Repository(Context context, DataSource localDataSource, DataSource remoteDataSource, DataSource fakeDataSource) {
        mContext = context;
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
        mFakeDataSource = fakeDataSource;

    }

    public static Repository getInstance(Context context, DataSource localDataSource, DataSource remoteDataSource, DataSource fakeDataSource) {

        if (INSTANCE == null) {
            INSTANCE = new Repository(context, localDataSource, remoteDataSource, fakeDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getAllLocation(final GeAllLocationCallback callback) {
        mLocalDataSource.getAllLocation(
                new GeAllLocationCallback() {
                    @Override
                    public void onSuccess(ArrayList<GeoLocation> data) {
                        callback.onSuccess(data);
                    }

                    @Override
                    public void failed(int errorCode, String msgError) {
                        callback.failed(errorCode, msgError);
                    }
                }
        );
    }

    @Override
    public void deleteAllLocationFromDB(final DeleteAllLocationCallBack callBack) {
        mLocalDataSource.deleteAllLocationFromDB(new DeleteAllLocationCallBack() {
            @Override
            public void onSuccess() {
                callBack.onSuccess();
            }

            @Override
            public void failed(int errorCode, String msgError) {
                callBack.failed(errorCode, msgError);
            }
        });
    }

    @Override
    public void saveLocation(GeoLocation beanLocation) {
        mLocalDataSource.saveLocation(beanLocation);
    }
}
