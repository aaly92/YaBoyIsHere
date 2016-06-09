package com.example.aaly.yaboyishere;

import android.app.PendingIntent;
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

    private static final long POINT_RADIUS = 15; // meters

    private static final long PROX_ALERT_EXPIRATION = -1;


    private static final String PROX_ALERT_INTENT = "PROX_INTENT";

    private String currentLocation = "";
    private Location pointLocation;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.pointLocation = intent.getExtras().getParcelable("location");
        addProximityAlert();
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

    private void addProximityAlert() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(PROX_ALERT_INTENT);
            PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locManager.addProximityAlert(
                    this.pointLocation.getLatitude(),
                    this.pointLocation.getLongitude(),

                    POINT_RADIUS,
                    PROX_ALERT_EXPIRATION,
                    proximityIntent
            );
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            float distance = location.distanceTo(pointLocation);
            SlackPost slackPost = null;
            if (distance <= POINT_RADIUS && !currentLocation.equals("INSIDE")) {
                slackPost = new SlackPost("Work Work Work", "BAD_GYAL", ":badgyal:");
                currentLocation = "INSIDE";
            } else if (distance > POINT_RADIUS && !currentLocation.equals("OUTSIDE")) {
                slackPost = new SlackPost("You need to get done done done at work", "BAD_GYAL", ":badgyal:");
                currentLocation = "OUTSIDE";
            }
            if (slackPost != null) {
                SlackPostAPI.Factory.getInstance().postToSlack(slackPost).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("Response", response.toString());
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
