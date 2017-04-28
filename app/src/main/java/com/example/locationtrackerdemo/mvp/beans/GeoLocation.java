package com.example.locationtrackerdemo.mvp.beans;

import android.net.Uri;

import com.example.locationtrackerdemo.mvp.providers.GeoLocationsContentProvider;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lokesh on 14/10/15.
 */
public class GeoLocation {
    private transient int id;

    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("accuracy")
    private double accuracy;

    public GeoLocation() {
    }

    public GeoLocation(double latitude, double longitude, double accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    public Uri getUri() {
        return Uri.parse(GeoLocationsContentProvider.CONTENT_URI + "/#" + getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "GeoLocation{" +
                "accuracy='" + accuracy + '\'' +
                ", id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
