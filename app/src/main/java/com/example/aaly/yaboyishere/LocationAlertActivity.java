package com.example.aaly.yaboyishere;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class LocationAlertActivity extends AppCompatActivity {

    private static final NumberFormat nf = new DecimalFormat("##.########");


    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Button findCoordinatesButton;
    private Button savePointButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        latitudeEditText = (EditText) findViewById(R.id.point_latitude);
        longitudeEditText = (EditText) findViewById(R.id.point_longitude);
        findCoordinatesButton = (Button) findViewById(R.id.find_coordinates_button);
        savePointButton = (Button) findViewById(R.id.save_point_button);

        findCoordinatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateCoordinatesFromLastKnownLocation();
            }
        });

        savePointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProximityAlertPoint();
            }
        });
    }

    private void populateCoordinatesFromLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitudeEditText.setText(nf.format(location.getLatitude()));
                longitudeEditText.setText(nf.format(location.getLongitude()));
            }
        }

    }

    private void saveProximityAlertPoint() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location location;
            String latitude = latitudeEditText.getText().toString();
            String longitude = longitudeEditText.getText().toString();

            if(!latitude.equals("") && !longitude.equals("") ){
                location = new Location("UserInput");
                location.setLatitude(Double.parseDouble(latitude));
                location.setLongitude(Double.parseDouble(longitude));
            } else {
                location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (location == null) {
                Toast.makeText(this, "No last known location. Aborting...",
                        Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(LocationAlertActivity.this, LocationService.class);
            intent.putExtra("location", location);
            startService(intent);
        }
    }

}

