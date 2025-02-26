package com.example.sahayakapp;

public class Post {
    private int id;
    private String text;
    private String imageUrl;
    private String userName;

    public Post(int id, String text, String imageUrl, String userName) {
        this.id = id;
        this.text = text;
        this.imageUrl = imageUrl;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserName() {
        return userName;
    }
}
