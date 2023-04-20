package ru.ap.entities;

public class Card {

    private long id;

    private String cardNumber;

    private long bankId;

    private long ownerId;

    public Card() {
    }

    public Card(String cardNumber, long bankId, long ownerId) {
        this.cardNumber = cardNumber;
        this.bankId = bankId;
        this.ownerId = ownerId;
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

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return id + ". " + cardNumber;
    }
}
