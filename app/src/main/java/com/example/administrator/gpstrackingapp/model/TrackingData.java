package com.example.administrator.gpstrackingapp.model;

/**
 * Created by administrator on 1/10/18.
 */

public class TrackingData {

    public String  Time;
    public float  latitude;
    public float  longitude;
    public int _id;

    public TrackingData() {


        this._id=_id;
        this.latitude=latitude;
        this.longitude=longitude;
    }


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "TrackingData{" +
                "Time='" + Time + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", _id=" + _id +

                '}';
    }
}
