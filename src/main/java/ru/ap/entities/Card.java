package ru.ap.entities;

public class Card {

    private long id;

    private String cardNumber;

    private long bantId;

    private long personId;

    public Card() {
    }

    public Card(String cardNumber, long bantId, long personId) {
        this.cardNumber = cardNumber;
        this.bantId = bantId;
        this.personId = personId;
    }

    public long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public long getBantId() {
        return bantId;
    }

    public void setBantId(long bantId) {
        this.bantId = bantId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return id + ". " + cardNumber;
    }
}
