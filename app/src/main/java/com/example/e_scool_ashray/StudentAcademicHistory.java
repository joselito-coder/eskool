package com.example.e_scool_ashray;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentAcademicHistory extends AppCompatActivity {

    private PieChartView totalMarksChart;
    private PieChartView isa1Chart;
    private PieChartView isa2Chart;
    private PieChartView isa3Chart;
    private PieChartView seeChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_academic_history);

        totalMarksChart = findViewById(R.id.totalMarksChart);
        isa1Chart = findViewById(R.id.isa1Chart);
        isa2Chart = findViewById(R.id.isa2Chart);
        isa3Chart = findViewById(R.id.isa3Chart);
        seeChart = findViewById(R.id.seeChart);

        setupPieCharts();
    }

    private void setupPieCharts() {
        // Total Marks
        totalMarksChart.setData(55f, 100f);
        ((TextView) findViewById(R.id.totalMarksText)).setText("55/100");

        // ISA 1
        isa1Chart.setData(15f, 20f);
        ((TextView) findViewById(R.id.isa1MarksText)).setText("15/20");

        // ISA 2
        isa2Chart.setData(10f, 20f);
        ((TextView) findViewById(R.id.isa2MarksText)).setText("10/20");

        // ISA 3
        isa3Chart.setData(10f, 20f);
        ((TextView) findViewById(R.id.isa3MarksText)).setText("10/20");

        // SEE
        seeChart.setData(20f, 40f);
        ((TextView) findViewById(R.id.seeMarksText)).setText("20/40");
    }

}
