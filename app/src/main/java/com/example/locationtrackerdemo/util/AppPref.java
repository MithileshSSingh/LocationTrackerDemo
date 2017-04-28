package com.example.locationtrackerdemo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mithilesh on 8/25/16.
 */
public class AppPref {

    private static final String preferanceName = "LOCATION_TRACKER";
    private SharedPreferences sp = null;

    private static AppPref INSTANCE = null;

    private AppPref(Context context) {
        sp = context.getSharedPreferences(preferanceName, Context.MODE_PRIVATE);
    }

    public static AppPref getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppPref(context);
        }
        return INSTANCE;
    }

    /**
     * Shared Preferance Key
     */
    public static class Keys {
        public static final String IS_TRACKING = "is_traking";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sp.getString(key, "");
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public Integer getInt(String key) {
        return sp.getInt(key, 0);
    }

    public void putFloat(String key, Float value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public Float getFloat(String key) {
        return sp.getFloat(key, 0f);
    }

    public void putLong(String key, Long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return sp.getLong(key, 0);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }


}
