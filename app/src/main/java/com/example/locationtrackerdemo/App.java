package com.example.locationtrackerdemo;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.example.locationtrackerdemo.mvp.locationupdate.LocationHelper;
import com.facebook.stetho.Stetho;

/**
 * Created by mithilesh on 4/27/17.
 */
public class App extends MultiDexApplication {

    private static App mInstance = null;
    private LocationHelper mLocationHelper;

    public static synchronized App getInstance() {
        if (mInstance == null) {
            mInstance = new App();
        }
        return mInstance;
    }

    public LocationHelper getLocationHelper(Context context) {

        if (mLocationHelper == null) {
            mLocationHelper = new LocationHelper(context);
        }

        return mLocationHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initServices();
    }

    private void initServices() {
        initStetho();
    }

    private void initStetho() {
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);

        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));

        Stetho.Initializer initializer = initializerBuilder.build();

        Stetho.initialize(initializer);
    }
}
