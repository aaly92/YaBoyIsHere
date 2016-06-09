package com.example.aaly.yaboyishere;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {


    private static final long POINT_RADIUS = 15; //meters

    private Location pointLocation;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            float distance = location.distanceTo(pointLocation);
            if(distance <=  POINT_RADIUS) {
                Log.v("INSIDE","Proximity Alert Intent Received");
            } else {
                Log.v("OUTSIDE","Proximity Alert Intent Received");
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
