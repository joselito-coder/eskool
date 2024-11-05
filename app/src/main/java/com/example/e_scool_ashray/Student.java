package com.example.e_scool_ashray;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Student extends AppCompatActivity {
        private FirebaseAuth mAuth;
        private EditText editTextUsername, editTextPassword;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_student);

            mAuth = FirebaseAuth.getInstance();
            editTextUsername = findViewById(R.id.editTextUsername);
            editTextPassword = findViewById(R.id.editTextPassword);
            Button buttonLogin = findViewById(R.id.buttonLogin);

            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginUser();
                }
            });

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
                            // Sign-in success
                            Intent intent = new Intent(Student.this, StudentHome.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign-in fails, display a message to the user.
                            Toast.makeText(Student.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


        private void checkUserLoggedIn() {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(Student.this, StudentHome.class);
                startActivity(intent);
                finish();
            }
        }
    }
