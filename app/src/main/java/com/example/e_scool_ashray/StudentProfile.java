package com.example.e_scool_ashray;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class StudentProfile extends AppCompatActivity {

        private TextView textViewUserName;
        private Button buttonLogout;
        private FirebaseAuth firebaseAuth;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_student_profile);

            textViewUserName = findViewById(R.id.textViewUserName);
            buttonLogout = findViewById(R.id.buttonLogout);
            firebaseAuth = FirebaseAuth.getInstance();

            // Display the logged-in user's email
            if (firebaseAuth.getCurrentUser() != null) {
                String userEmail = firebaseAuth.getCurrentUser().getEmail();
                textViewUserName.setText(userEmail);
            } else {
                textViewUserName.setText("No user is logged in");
            }

            // Set up the logout button
            buttonLogout.setOnClickListener(v -> logout());
        }

        private void logout() {
            firebaseAuth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            // Redirect to login activity or main activity
            Intent intent = new Intent(StudentProfile.this, MainActivity.class); // Replace with your login activity
            startActivity(intent);
            finish(); // Close the profile activity
        }
    }
