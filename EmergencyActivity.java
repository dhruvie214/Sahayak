package com.example.sahayakapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class EmergencyActivity extends AppCompatActivity {

    private static final String POLICE_NUMBER = "100";
    private static final String FIRE_NUMBER = "101";
    private static final String AMBULANCE_NUMBER = "102";
    private static final String WOMEN_HELPLINE_NUMBER = "1091";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private String lastKnownLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();

        // Initialize Call & Message Buttons
        ImageView callPolice = findViewById(R.id.callPolice);
        ImageView msgPolice = findViewById(R.id.msgPolice);
        ImageView callFire = findViewById(R.id.callFire);
        ImageView msgFire = findViewById(R.id.msgFire);
        ImageView callAmbulance = findViewById(R.id.callAmbulance);
        ImageView msgAmbulance = findViewById(R.id.msgAmbulance);
        ImageView callDisaster = findViewById(R.id.callDisaster);
        ImageView msgDisaster = findViewById(R.id.msgDisaster);

        // Set Click Listeners for Calling
        callPolice.setOnClickListener(v -> makeCall(POLICE_NUMBER));
        callFire.setOnClickListener(v -> makeCall(FIRE_NUMBER));
        callAmbulance.setOnClickListener(v -> makeCall(AMBULANCE_NUMBER));
        callDisaster.setOnClickListener(v -> makeCall(WOMEN_HELPLINE_NUMBER));

        // Set Click Listeners for Messaging
        msgPolice.setOnClickListener(v -> sendSMS(POLICE_NUMBER, "Help! I need immediate police assistance."));
        msgFire.setOnClickListener(v -> sendSMS(FIRE_NUMBER, "Fire emergency! Need immediate help."));
        msgAmbulance.setOnClickListener(v -> sendSMS(AMBULANCE_NUMBER, "Medical emergency! Need an ambulance."));
        msgDisaster.setOnClickListener(v -> sendSMS(WOMEN_HELPLINE_NUMBER, "I need help from the women helpline."));

        // Find Safe Zones
        findViewById(R.id.findSafeZonesButton).setOnClickListener(this::findSafeZones);

        // Send Live Location via SMS
        findViewById(R.id.shareLocationButton).setOnClickListener(this::sendLiveLocation);
    }

    private void makeCall(String number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    private void sendSMS(String number, String message) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:" + number));
        smsIntent.putExtra("sms_body", message);
        startActivity(smsIntent);
    }

    public void findSafeZones(View view) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=Disaster+Relief+Shelter+OR+Emergency+Aid+Center+OR+Hospital");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void sendLiveLocation(View view) {
        if (lastKnownLocation.isEmpty()) {
            Toast.makeText(this, "Fetching location...", Toast.LENGTH_SHORT).show();
            getLastLocation();
        } else {
            sendSMS(WOMEN_HELPLINE_NUMBER, "Emergency! My location: " + lastKnownLocation);
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                        @Override
                        public void onSuccess(android.location.Location location) {
                            if (location != null) {
                                lastKnownLocation = "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                            } else {
                                lastKnownLocation = "Unable to fetch location";
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission required for live location sharing", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
