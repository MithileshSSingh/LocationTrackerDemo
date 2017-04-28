package com.example.locationtrackerdemo.mvp.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.locationtrackerdemo.data.local.DBHelper;

import java.util.HashMap;

public abstract class BaseContentProvider extends ContentProvider {

    //OVERRIDE THIS
    protected String CONTENT_NAME;
    protected String CONTENT_TYPE;
//	protected String CONTENT_URL;

    protected static final int CASE_DIRECTORY = 1;
    protected static final int CASE_ITEM = 2;
    protected UriMatcher uriMatcher;

    protected DBHelper dbHelper;
    // projection map for a query
    protected HashMap<String, String> requestMap;

    //database declarations
    protected SQLiteDatabase database;

    //OVERRIDE THESE
    protected String TABLE_NAME;
    protected String PRIMARY_KEY;


    @Override
    public boolean onCreate() {
        Log.v("BaseContentProvider", "onCreate " + CONTENT_TYPE + "/#" + CASE_ITEM);

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_NAME, CONTENT_TYPE, CASE_DIRECTORY);
        uriMatcher.addURI(CONTENT_NAME, CONTENT_TYPE + "/#", CASE_ITEM);

        dbHelper = new DBHelper(getContext());
        // permissions to be writable
        database = dbHelper.getWritableDatabase();
        return database != null;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // the TABLE_NAME to query on
        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            // maps all database column names
            case CASE_DIRECTORY:
                queryBuilder.setProjectionMap(requestMap);
                break;
            case CASE_ITEM:
                queryBuilder.appendWhere(PRIMARY_KEY + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder.equals("")) {
            // No sorting-> sort on names by default
            sortOrder = PRIMARY_KEY;
        }

        Cursor cursor = queryBuilder.query(database, projection, selection,
                selectionArgs, null, null, sortOrder);

        /**
         * register to watch a content URI for changes
         */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            // Get all friend-birthday records
            case CASE_DIRECTORY:
                return "vnd.android.cursor.dir/vnd.locationtrackerdemo." + CONTENT_TYPE;
            // Get a particular friend
            case CASE_ITEM:
                return "vnd.android.cursor.item/vnd.locationtrackerdemo" + CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long row = database.insert(TABLE_NAME, "", values);

        // If record is added successfully
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);        //notify about the change to the directory
            return ContentUris.withAppendedId(uri, row);
        }
        //		throw new SQLException("Fail to add a new record into " + uri);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;

        switch (uriMatcher.match(uri)) {
            case CASE_DIRECTORY:
                count = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case CASE_ITEM:
                String id = uri.getLastPathSegment();    //gets the id
                count = database.delete(TABLE_NAME, PRIMARY_KEY + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;

        switch (uriMatcher.match(uri)) {
            case CASE_DIRECTORY:
                count = database.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case CASE_ITEM:
                count = database.update(TABLE_NAME, values, PRIMARY_KEY +
                        " = " + uri.getLastPathSegment() +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
}
