package ru.ap.entities;

public class Card {

    private long id;

    private String cardNumber;

    private long bankId;

    private long personId;

    public Card() {
    }

    public Card(String cardNumber, long bankId, long personId) {
        this.cardNumber = cardNumber;
        this.bankId = bankId;
        this.personId = personId;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
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
