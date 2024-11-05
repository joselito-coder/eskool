package com.example.e_scool_ashray;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Check if user is already logged in
        if (currentUser != null) {
            // User is logged in, navigate to TeacherHome or StudentHome based on user type
            Intent intent;
            // Example logic, adjust according to your implementation
            if (isTeacher(currentUser)) {  // Implement isTeacher() to check user type
                intent = new Intent(MainActivity.this, TeacherHome.class);
            } else {
                intent = new Intent(MainActivity.this, StudentHome.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
            startActivity(intent);
            finish();
            return; // Exit the method to avoid executing the rest of onCreate
        }

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent for Button 1
                Intent intent1 = new Intent(MainActivity.this, Teacher.class);
                startActivity(intent1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent for Button 2
                Intent intent2 = new Intent(MainActivity.this, Student.class);
                startActivity(intent2);
            }
        });
    }

    // Example method to determine if the user is a teacher
    private boolean isTeacher(FirebaseUser user) {
        // Implement your logic here to determine if the user is a teacher
        // This could involve checking user roles stored in your database
        return true; // Placeholder return value
    }
}
