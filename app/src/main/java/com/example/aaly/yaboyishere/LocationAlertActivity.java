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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationAlertActivity extends AppCompatActivity {

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("##.########");
    private static final String LOCATION_EXTRA_FROM_INTENT = "Intent Location";

    @BindView(R.id.point_latitude)
    EditText latitudeEditText;
    @BindView(R.id.point_longitude)
    EditText longitudeEditText;
    @BindView(R.id.find_coordinates_button)
    Button findCoordinatesButton;
    @BindView(R.id.save_point_button)
    Button savePointButton;

    @OnClick(R.id.find_coordinates_button)
    public void findCoordinatesButton() {
        populateCoordinatesFromLastKnownLocation();
    }

    @OnClick(R.id.save_point_button)
    public void onSaveButtonClick() {
        saveProximityAlertPoint();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
    }


    private void populateCoordinatesFromLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitudeEditText.setText(NUMBER_FORMAT.format(location.getLatitude()));
                longitudeEditText.setText(NUMBER_FORMAT.format(location.getLongitude()));
            }
        }
    }

    private void saveProximityAlertPoint() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location location;
            String latitude = latitudeEditText.getText().toString();
            String longitude = longitudeEditText.getText().toString();

            if (!latitude.equals("") && !longitude.equals("")) {
                location = new Location("UserInput");
                location.setLatitude(Double.parseDouble(latitude));
                location.setLongitude(Double.parseDouble(longitude));
            } else {
                location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    Toast.makeText(this, "No last known location. Aborting...",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    latitudeEditText.setText(NUMBER_FORMAT.format(location.getLatitude()));
                    longitudeEditText.setText(NUMBER_FORMAT.format(location.getLongitude()));
                }
            }

            Intent intent = new Intent(LocationAlertActivity.this, LocationService.class);
            intent.putExtra(LOCATION_EXTRA_FROM_INTENT, location);
            startService(intent);
        }
    }

}

