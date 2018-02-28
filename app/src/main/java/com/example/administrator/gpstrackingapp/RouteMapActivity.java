package com.example.administrator.gpstrackingapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.administrator.gpstrackingapp.db.DbHelper;
import com.example.administrator.gpstrackingapp.model.TrackingData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.graphics.Color;


import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RouteMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final String TAG = RouteMapActivity.class.getSimpleName();
    protected GoogleMap map;
    List<TrackingData> trackingdatainfo;
    public static String trackingdata_url="http://gofenzbeta.azurewebsites.net/api/Mobile/GetTrackingData";
    LocationManager locationManager;
    private TrackingData first_position;
    private TrackingData last_position;
    private float startlat, startlongi, lastLat, lastlong;
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        trackingdatainfo=new ArrayList<>();
        dbHelper=new DbHelper(RouteMapActivity.this);


                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        trackingdata_url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());

                                try {

                                    JSONArray tripData = response.getJSONArray("TripData");


                                    for (int i = 0; i < tripData.length(); i++) {

                                        JSONObject data_object = tripData.getJSONObject(i);
                                        JSONArray tripList = data_object.getJSONArray("TripList");

                                        for (int j = 0; j < tripList.length(); j++) {
                                            JSONObject tripList_object = tripList.getJSONObject(j);

                                            TrackingData trackingData = new TrackingData();

                                            trackingData.longitude = Float.valueOf(tripList_object.getString("Longitude"));
                                            trackingData.latitude = Float.valueOf(tripList_object.getString("Latitude"));


                                            trackingdatainfo.add(trackingData);
                                            dbHelper.addProduct(trackingData);

                                        }
                                    }
                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }

                                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
                                mapFragment.getMapAsync(RouteMapActivity.this);
                                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                if (ActivityCompat.checkSelfPermission(RouteMapActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RouteMapActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, RouteMapActivity.this);
Log.i("dbdata","...."+dbHelper.getAllProducts().toString());

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());

                    }
                });


        AppController.getInstance().addToRequestQueue(jsonObjReq);



    }




    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;



        first_position = trackingdatainfo.get(0);
        last_position = trackingdatainfo.get(trackingdatainfo.size() -1);

        if(first_position != null) {
         startlat = first_position.latitude;
          startlongi = first_position.longitude;
        }

        if(last_position != null) {
            lastLat = last_position.latitude;
            lastlong = last_position.longitude;
        }


        LatLng origin = new LatLng(startlat, startlongi);
        LatLng destination = new LatLng(lastLat, lastlong);
        DrawMarker.getInstance(this).draw(map, origin, R.drawable.marker, "Starting Location");
        DrawMarker.getInstance(this).draw(map, destination, R.drawable.marker, "Destination Location");


        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();

        ArrayList points = new ArrayList();
        

        for (int j = 0; j < trackingdatainfo.size(); j++) {
            TrackingData point = trackingdatainfo.get(j);

            float lat = point.latitude;
            float lng = point.longitude;
            LatLng position = new LatLng(lat, lng);

            points.add(position);
        }

        map.addPolyline(new PolylineOptions()
                .addAll(points)
                .width(8)
                    .geodesic(true)
                .color(Color.rgb(49, 130, 87)));


        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));

        Marker marker = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_local_car_wash_black_24dp)))
                .position(new LatLng(trackingdatainfo.get(0).latitude, trackingdatainfo.get(0).longitude))
                .flat(true));


        animateMarker(map, marker, trackingdatainfo);
    }

    private static void animateMarker(final GoogleMap myMap, final Marker marker, final List<TrackingData> directionPoint) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 600000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);


                if (i < directionPoint.size()) {
                        marker.setPosition(new LatLng(directionPoint.get(i).latitude, directionPoint.get(i).longitude));
                    myMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(directionPoint.get(i).latitude, directionPoint.get(i).longitude)));

                    i++;

                }
                if (t < 1.0) {
                    handler.postDelayed(this, 10000);
                }
                if(i==directionPoint.size()){

                    handler.removeCallbacks(this);

                }


            }
        });
    }


}




