package com.example.locationtrackerdemo.mvp.screen_home;

import com.example.locationtrackerdemo.mvp.BasePresenter;
import com.example.locationtrackerdemo.mvp.BaseView;
import com.example.locationtrackerdemo.mvp.beans.GeoLocation;

import java.util.ArrayList;

/**
 * Created by mithilesh on 9/11/16.
 */
public interface HomeContract {
    interface OnItemSelectedListener {
        void onItemSelected(int position);

    }

    interface View extends BaseView<Presenter> {

        interface GeAllLocationCallback {
            void onSuccess(ArrayList<GeoLocation> data);

            void failed(int errorCode, String msgError);
        }

        interface DeleteAllLocationCallBack {
            void onSuccess();

            void failed(int errorCode, String msgError);
        }
    }

    interface Presenter extends BasePresenter {

        void getAllLocation(View.GeAllLocationCallback callback);

        void deleteAllLocationFromDB(View.DeleteAllLocationCallBack callBack);

        void saveLocation(GeoLocation beanLocation);
    }
}
