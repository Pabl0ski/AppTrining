package com.example.trainiglistapp;
public class Exercise {
    private String name;
    private String type;
    private String level;

    // Constructor
    public Exercise(String name, String type, String level) {
        this.name = name;
        this.type = type;
        this.level = level;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLevel() {
        return level;
    }
}
