package com.example.aaly.yaboyishere;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.aaly.yaboyishere.data.remote.SlackPostAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATE = 1; // meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // milliseconds
    private static final long POINT_RADIUS = 1000; // meters


    private Boolean isInside = null;
    private Location pointLocation;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.isInside = null;
        this.pointLocation = intent.getExtras().getParcelable("location");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MINIMUM_TIME_BETWEEN_UPDATE,
                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATE,
                    new MyLocationListener()
            );
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            float distance = location.distanceTo(pointLocation);
            SlackPost slackPost = null;
            if (distance <= POINT_RADIUS && (isInside == null || !isInside)) {
                slackPost = new SlackPost("I’m in the building and I’m feeling myself", "Drizzy", ":drizzy:");
                isInside = true;
            } else if (distance > POINT_RADIUS && (isInside == null || isInside)) {
                slackPost = new SlackPost("I'm leaving I'm gone", "Drizzy", ":drizzy:");
                isInside = false;
            }
            if (slackPost != null) {
                SlackPostAPI.Factory.getInstance().postToSlack(slackPost).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Log.d("Response", "SUCCESS");
                        } else {
                            Log.d("Error", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Failed", t.getMessage());
                    }
                });
            }
        }

        public void onStatusChanged(String s, int i, Bundle b) {
        }

        public void onProviderDisabled(String s) {
        }

        public void onProviderEnabled(String s) {
        }
    }
}
