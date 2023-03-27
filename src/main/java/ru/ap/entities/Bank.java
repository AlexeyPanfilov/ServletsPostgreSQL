package ru.ap.entities;

public class Bank {

    private long id;

    private String title;

    public Bank() {
    }

    public Bank(long id, String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return id + ". " + title;
    }
}
