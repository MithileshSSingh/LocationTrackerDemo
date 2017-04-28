package com.example.locationtrackerdemo.mvp.screen_home;

import com.example.locationtrackerdemo.data.DataSource;
import com.example.locationtrackerdemo.data.Repository;
import com.example.locationtrackerdemo.mvp.beans.GeoLocation;

import java.util.ArrayList;

/**
 * Created by mithilesh on 9/11/16.
 */
public class HomePresenter implements HomeContract.Presenter {
    private Repository mRepository;
    private HomeContract.View mView;

    private ArrayList<String> listSubReddits = new ArrayList<>();

    public HomePresenter(Repository repository, HomeContract.View view) {
        this.mRepository = repository;
        this.mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void getAllLocation(final HomeContract.View.GeAllLocationCallback callback) {
        mRepository.getAllLocation(new DataSource.GeAllLocationCallback() {
            @Override
            public void onSuccess(ArrayList<GeoLocation> data) {
                callback.onSuccess(data);
            }

            @Override
            public void failed(int errorCode, String msgError) {
                callback.failed(errorCode, msgError);
            }
        });
    }

    @Override
    public void deleteAllLocationFromDB(final HomeContract.View.DeleteAllLocationCallBack callBack) {

        mRepository.deleteAllLocationFromDB(new DataSource.DeleteAllLocationCallBack() {
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

    }
}
