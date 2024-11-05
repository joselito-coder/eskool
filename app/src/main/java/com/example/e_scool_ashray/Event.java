package com.example.e_scool_ashray;

public class Event {
    private String date;
    private String user;
    private String eventName;

    public Event() {} // Firestore needs a public no-argument constructor

    public Event(String date, String user, String eventName) {
        this.date = date;
        this.user = user;
        this.eventName = eventName;
    }

    public String getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public String getEventName() {
        return eventName;
    }
}
