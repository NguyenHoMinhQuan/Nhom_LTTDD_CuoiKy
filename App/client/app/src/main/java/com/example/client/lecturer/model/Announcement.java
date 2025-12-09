package com.example.client.lecturer.model;

import java.io.Serializable;

public class Announcement implements Serializable {
    private String title;
    private String body;
    private String author;
    private String createdAt;

    // Constructor
    public Announcement(String title, String body, String author, String createdAt) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.createdAt = createdAt;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
