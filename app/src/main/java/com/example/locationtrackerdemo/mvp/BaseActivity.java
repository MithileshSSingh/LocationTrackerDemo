package com.example.locationtrackerdemo.mvp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.locationtrackerdemo.R;
import com.example.locationtrackerdemo.mvp.screen_home.HomeFragment;
import com.example.locationtrackerdemo.util.AppConstants;


/**
 * Created by mithilesh on 8/14/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static String TAG = "TAG";
    public Resources mResources;
    public Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mActivity = this;
        mResources = this.getResources();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected abstract void init();

    protected abstract void initView();

    protected abstract void initMembers();

    protected abstract void initListeners();

    protected abstract void initData();


    public void showFragment(int screenId, Bundle data) {

        String tag = null;
        Fragment fragment = null;
        boolean addToBackStack = false;

        switch (screenId) {
            case AppConstants.Screen.SCREEN_ID_HOME:

                fragment = HomeFragment.newInstance();
                tag = HomeFragment.TAG;

                break;
            default:
                break;
        }

        if (fragment != null && tag != null) {
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    fragment, R.id.content,
                    tag, addToBackStack);
        }
    }

}
