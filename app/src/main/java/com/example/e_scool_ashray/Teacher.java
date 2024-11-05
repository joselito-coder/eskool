package com.example.e_scool_ashray;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Teacher extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> loginUser());

        checkUserLoggedIn();
    }

    private void loginUser() {
        String email = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success, check user role
                        checkUserRole(email);
                    } else {
                        // If sign-in fails, display a message to the user.
                        Toast.makeText(Teacher.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(String email) {
        db.collection("users").document(email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String role = task.getResult().getString("role");
                        if ("admin".equals(role)) {
                            // User is an admin, proceed to TeacherHome
                            Intent intent = new Intent(Teacher.this, TeacherHome.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
                            startActivity(intent);
                            finish();
                        } else {
                            // User is not an admin, show a message
                            Toast.makeText(Teacher.this, "Access denied. Admins only.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Teacher.this, "Failed to retrieve user role.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, check role
            checkUserRole(currentUser.getEmail());
        }
    }
}
