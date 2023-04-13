package ru.ap.entities;

import java.util.List;

public class Bank {

    private long id;

    private String title;

    private List<Person> persons;

    private List<Card> cards;

    public Bank() {
    }

    public Bank(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return id + ". " + title;
    }
}
