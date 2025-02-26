package com.example.sahayakapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText etFullName, etEmail, etPassword, etContact, etLocation;
    private Button btnRegister;
    private TextView tvLogin;
    private Spinner spinnerSkills;
    private RadioGroup rgUserType;
    private RadioButton rbUser, rbVolunteer;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etContact = findViewById(R.id.etContact);
        etLocation = findViewById(R.id.etLocation);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        spinnerSkills = findViewById(R.id.spinnerSkills);
        rgUserType = findViewById(R.id.rgUserType);
        rbUser = findViewById(R.id.rbUser);
        rbVolunteer = findViewById(R.id.rbVolunteer);

        // Initialize the SQLite database helper
        dbHelper = new DBHelper(this);

        // Toggle skills spinner visibility based on user type selection
        rgUserType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbVolunteer) {
                spinnerSkills.setVisibility(android.view.View.VISIBLE);
            } else {
                spinnerSkills.setVisibility(android.view.View.GONE);
            }
        });

        // Set onClick listener for the Register button
        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String contact = etContact.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String userType = rbVolunteer.isChecked() ? "Volunteer" : "User";
            String skills = rbVolunteer.isChecked() ? spinnerSkills.getSelectedItem().toString() : "N/A";

            // Check if any field is empty
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || contact.isEmpty() || location.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Attempt to register user in the SQLite database
                boolean inserted = dbHelper.registerUser(fullName, email, password, contact, location, userType, skills);
                if (inserted) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error: Email already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set onClick listener for the Login text view to navigate to LoginActivity
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
