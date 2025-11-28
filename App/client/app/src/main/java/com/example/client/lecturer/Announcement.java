package com.example.client.lecturer;

import java.io.Serializable;

public class Announcement implements Serializable {
    private String title;
    private String summary;
    private String author;
    private String date;

    // Constructor
    public Announcement(String title, String summary, String author, String date) {
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.date = date;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }
}
