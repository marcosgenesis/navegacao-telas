package com.example.trab2;

import java.io.Serializable;

public class TaskModel implements Serializable {
    int id;
    String title;
    String description;

    public TaskModel(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "" + getId() + " - " + getTitle() + "\n   " + getDescription();
    }
}
