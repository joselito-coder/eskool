package com.example.e_scool_ashray;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class StudentAttendance extends AppCompatActivity {

    private ListView attendanceListView;
    private ArrayList<String> attendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        // Initialize views and data
        attendanceListView = findViewById(R.id.attendanceListView);
        attendanceList = new ArrayList<>();

        // Load attendance data
        loadAttendanceData();

        // Set up the adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                attendanceList
        );
        attendanceListView.setAdapter(adapter);
    }

    private void loadAttendanceData() {
        // Replace with actual data retrieval logic in a real application
        attendanceList.add("1st November: Absent");
        attendanceList.add("2nd November: Holiday");
        attendanceList.add("3rd November: Holiday");
        attendanceList.add("4th November: Present");
        
    }
}
