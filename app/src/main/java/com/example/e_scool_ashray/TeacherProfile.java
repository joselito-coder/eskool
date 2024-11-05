package com.example.e_scool_ashray;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.EditText;
import android.widget.CheckBox;

import java.util.HashMap;
import java.util.Map;

// ... existing imports

public class TeacherProfile extends AppCompatActivity {

    // Existing fields
    private TextView textViewUserName;
    private Button buttonLogout, buttonSignUp;
    private FirebaseAuth firebaseAuth;

    // New fields for signup
    private EditText editTextEmail, editTextPassword;
    private CheckBox checkBoxAdmin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        textViewUserName = findViewById(R.id.textViewUserName);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkBoxAdmin = findViewById(R.id.checkBoxAdmin);
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

        // Set up the sign-up button
        buttonSignUp.setOnClickListener(v -> signUp());
    }

    private void signUp() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        boolean isAdmin = checkBoxAdmin.isChecked();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user with Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User created successfully
                        saveUserRole(email, isAdmin);
                        Toast.makeText(TeacherProfile.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(TeacherProfile.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserRole(String email, boolean isAdmin) {
        // Store user role in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("role", isAdmin ? "admin" : "user");

        db.collection("users")
                .document(email) // Use email as document ID
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("TAG", "User role added successfully"))
                .addOnFailureListener(e -> Log.w("TAG", "Error adding user role", e));
    }

    private void logout() {
        firebaseAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(TeacherProfile.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
