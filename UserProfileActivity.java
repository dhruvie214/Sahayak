package com.example.sahayakapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvEmail;
    private EditText etFullName, etContact, etLocation;
    private Button btnUpdateProfile, btnChangePassword;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvEmail = findViewById(R.id.tvEmail);
        etFullName = findViewById(R.id.etFullName);
        etContact = findViewById(R.id.etContact);
        etLocation = findViewById(R.id.etLocation);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        dbHelper = new DBHelper(this);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        loadUserData();

        btnUpdateProfile.setOnClickListener(v -> updateProfile());
        btnChangePassword.setOnClickListener(v -> openChangePasswordScreen());
    }

    private void loadUserData() {
        String email = sharedPreferences.getString("userEmail", "N/A");
        String fullName = dbHelper.getUserName(email);
        String contact = dbHelper.getUserContact(email);
        String location = dbHelper.getUserLocation(email);

        tvEmail.setText(email);
        etFullName.setText(fullName);
        etContact.setText(contact);
        etLocation.setText(location);
    }

    private void updateProfile() {
        String email = sharedPreferences.getString("userEmail", "N/A");
        String fullName = etFullName.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (fullName.isEmpty() || contact.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = dbHelper.updateUserProfile(email, fullName, contact, location);
        if (updated) {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Update failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openChangePasswordScreen() {
        Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}
