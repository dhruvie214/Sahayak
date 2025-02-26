package com.example.sahayakapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DashboardActivity extends AppCompatActivity {

    private TextView welcomeMessage;
    private CardView cardEmergencyContacts, cardVolunteerNetwork, cardCommunityBoard, cardWeatherTraffic, cardUserProfile;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize UI Components
        welcomeMessage = findViewById(R.id.welcomeMessage);
        cardEmergencyContacts = findViewById(R.id.cardEmergencyContacts);
        cardVolunteerNetwork = findViewById(R.id.cardVolunteerNetwork);
        cardCommunityBoard = findViewById(R.id.cardCommunityBoard);
        cardWeatherTraffic = findViewById(R.id.cardWeatherTraffic);
        cardUserProfile = findViewById(R.id.cardUserProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Set Click Listeners for Dashboard Cards
        cardEmergencyContacts.setOnClickListener(v -> openActivity(EmergencyActivity.class));
        cardVolunteerNetwork.setOnClickListener(v -> openActivity(VolunteerActivity.class));
        cardCommunityBoard.setOnClickListener(v -> openActivity(CommunityBoardActivity.class));
        cardWeatherTraffic.setOnClickListener(v -> openActivity(WeatherTrafficActivity.class));
        cardUserProfile.setOnClickListener(v -> openActivity(UserProfileActivity.class));

        // Logout Button Click Listener
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    // Function to Open Activities
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(DashboardActivity.this, activityClass);
        startActivity(intent);
    }
}
