package com.example.e_scool_ashray;


public class Book {
    private String bookName;
    private String email;
    private String tagName;
    private String downloadUrl;

    // Required empty constructor for Firestore
    public Book() {
    }

    public Book(String bookName, String email, String tagName, String downloadUrl) {
        this.bookName = bookName;
        this.email = email;
        this.tagName = tagName;
        this.downloadUrl = downloadUrl;
    }

    // Getters
    public String getBookName() {
        return bookName;
    }

    public String getEmail() {
        return email;
    }

    public String getTagName() {
        return tagName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
