package com.sse.demo.models;


public class Notification {

    private final String id;
    private final String message;

    public Notification(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
