package com.example.e_scool_ashray;


public class ChatMessage {
    private String senderEmail;
    private String message;
    private long timestamp;

    public ChatMessage() { } // Needed for Firestore

    public ChatMessage(String senderEmail, String message) {
        this.senderEmail = senderEmail;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
