package com.example.e_scool_ashray;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.cardview.widget.CardView; // Import CardView

public class TeacherHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize CardView items
        CardView cardProfile = findViewById(R.id.cardProfile);
        //CardView cardAttendance = findViewById(R.id.cardAttendance);
        CardView cardClassroom = findViewById(R.id.cardClassroom);
        CardView cardEventCalendar = findViewById(R.id.cardEventCalendar);
        CardView cardAcademicHistory = findViewById(R.id.cardAcademicHistory);
        CardView cardLibrary = findViewById(R.id.cardLibrary);

        // Set click listeners for each CardView
        cardProfile.setOnClickListener(v -> startActivity(new Intent(TeacherHome.this, TeacherProfile.class)));
        //cardAttendance.setOnClickListener(v -> startActivity(new Intent(TeacherHome.this, TeacherAttendance.class)));
        cardClassroom.setOnClickListener(v -> startActivity(new Intent(TeacherHome.this, Classroom.class)));





        cardEventCalendar.setOnClickListener(v -> startActivity(new Intent(TeacherHome.this, EventCalendar.class)));
        cardAcademicHistory.setOnClickListener(v -> startActivity(new Intent(TeacherHome.this, TeacherAcademicHistory.class)));
        cardLibrary.setOnClickListener(v -> startActivity(new Intent(TeacherHome.this, LibUpload.class)));
    }
}
