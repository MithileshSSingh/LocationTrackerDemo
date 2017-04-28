package com.example.locationtrackerdemo.mvp;

/**
 * Created by mithilesh on 8/18/16.
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
}
