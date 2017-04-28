package com.example.locationtrackerdemo.mvp.screen_home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.locationtrackerdemo.R;
import com.example.locationtrackerdemo.mvp.BaseActivity;
import com.example.locationtrackerdemo.util.AppConstants;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void init() {
        initView();
        initMembers();
        initListeners();
        initData();

    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initMembers() {
        showFragment(AppConstants.Screen.SCREEN_ID_HOME, null);
    }

    @Override
    protected void initListeners() {
    }

    @Override
    protected void initData() {

    }
}
