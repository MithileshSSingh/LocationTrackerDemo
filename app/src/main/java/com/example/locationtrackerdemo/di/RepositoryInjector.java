package com.example.locationtrackerdemo.di;

import android.content.Context;

import com.example.locationtrackerdemo.data.Repository;
import com.example.locationtrackerdemo.data.fake.FakeDataSource;
import com.example.locationtrackerdemo.data.local.LocalDataSource;
import com.example.locationtrackerdemo.data.remote.RemoteDataSource;


/**
 * Created by mithilesh on 9/4/16.
 */
public class RepositoryInjector {

    public static Repository provideRepository(Context context) {
        return Repository.getInstance(
                context,
                LocalDataSource.getInstance(context),
                RemoteDataSource.getInstance(context),
                FakeDataSource.getInstance(context)
        );
    }
}
