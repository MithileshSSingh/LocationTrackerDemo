package com.example.locationtrackerdemo.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.locationtrackerdemo.mvp.providers.GeoLocationsContentProvider;

/**
 * Created by mithilesh on 9/3/16.
 */
public class DBHelper {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private DBOpenHelper dbOpenHelper;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GroundPilot";

    public DBHelper(Context context) {
        this.context = context;
        dbOpenHelper = new DBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLiteDatabase getWritableDatabase() {
        return dbOpenHelper.getWritableDatabase();
    }

    private static final String CREATE_TABLE_GEOLOCATIONS =
            "create table " + GeoLocationsContentProvider.TABLE_NAME_GEOLOCATIONS + "("
                    + GeoLocationsContentProvider.KEY_ID + "        INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + GeoLocationsContentProvider.KEY_LATITUDE + "  double(15,8), "
                    + GeoLocationsContentProvider.KEY_LONGITUDE + " double(15,8), "
                    + GeoLocationsContentProvider.KEY_ACCURACY + "  double(15,8));";

    private class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.v(TAG, "DbOpenHelper.oncreate called");
            db.execSQL(CREATE_TABLE_GEOLOCATIONS);
            Log.v(TAG, "DbOpenHelper.oncreate done");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

}
