package com.example.e_scool_ashray;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventCalendar extends AppCompatActivity {
    private RecyclerView eventList;
    private EventAdapter eventAdapter;
    private List<Event> events = new ArrayList<>();
    private CalendarView calendarView;

    private int selectedYear;
    private int selectedMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_calendar);

        calendarView = findViewById(R.id.calendarView);
        eventList = findViewById(R.id.eventList);
        Button addEventButton = findViewById(R.id.addEventButton);
        Button todayButton = findViewById(R.id.todayButton);

        eventAdapter = new EventAdapter(events);
        eventList.setLayoutManager(new LinearLayoutManager(this));
        eventList.setAdapter(eventAdapter);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedYear = year;
            selectedMonth = month + 1; // month is 0-based
            loadEventsForMonth(selectedYear, selectedMonth);
        });

        addEventButton.setOnClickListener(v -> showAddEventDialog());

        todayButton.setOnClickListener(v -> {
            Calendar today = Calendar.getInstance();
            selectedYear = today.get(Calendar.YEAR);
            selectedMonth = today.get(Calendar.MONTH) + 1; // Month is 0-based

            calendarView.setDate(today.getTimeInMillis(), true, true);
            loadEventsForMonth(selectedYear, selectedMonth);
        });
    }

    private void loadEventsForMonth(int year, int month) {
        events.clear(); // Clear the existing list before loading new events

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("EventCalendar", "Fetching events for " + year + "-" + month);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            String[] eventParts = event.getDate().split("-");
                            int eventYear = Integer.parseInt(eventParts[0]);
                            int eventMonthStored = Integer.parseInt(eventParts[1]);

                            // Check if the event is in the selected month and year
                            if (eventYear == year && eventMonthStored == month) {
                                events.add(event); // Add to the list if it matches the criteria
                                Log.d("EventCalendar", "Added event: " + event.getEventName());
                            }
                        }
                        Log.d("EventCalendar", "Retrieved " + events.size() + " events for " + year + "-" + month);
                        eventAdapter.updateEvents(events); // Update the adapter after loading all events
                    } else {
                        Log.e("EventCalendar", "Error fetching events: ", task.getException());
                    }
                });
    }


    private void showAddEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
        builder.setView(dialogView);

        EditText eventNameEditText = dialogView.findViewById(R.id.eventNameEditText);
        EditText userNameEditText = dialogView.findViewById(R.id.userNameEditText);

        builder.setTitle("Add Event")
                .setPositiveButton("Add", (dialog, which) -> {
                    String eventName = eventNameEditText.getText().toString();
                    String userName = userNameEditText.getText().toString();
                    String selectedDate = getSelectedDate();

                    // Create the event
                    Event newEvent = new Event(selectedDate, userName, eventName);
                    events.add(newEvent);
                    eventAdapter.notifyDataSetChanged();

                    // Store the event in Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("events").add(newEvent)
                            .addOnSuccessListener(documentReference -> {
                                // Event added successfully
                            })
                            .addOnFailureListener(e -> {
                                // Handle the error
                            });
                })
                .setNegativeButton("Cancel", null);

        builder.show();
    }

    private String getSelectedDate() {
        return String.format("%04d-%02d-%02d", selectedYear, selectedMonth, 1); // Use first day of the month
    }
}
