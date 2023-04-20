package ru.ap.service;

import ru.ap.db.DataBase;
import ru.ap.entities.Card;
import ru.ap.repository.BankRepository;
import ru.ap.repository.CardRepository;
import ru.ap.repository.PersonRepository;

public class CardService {

    private final DataBase dataBase;
    private final CardRepository cardRepository;

    public CardService(DataBase dataBase) {
        this.dataBase = dataBase;
        this.cardRepository = new CardRepository(dataBase);
    }

    public String getCardInfoById(long id) {
        Card card = cardRepository.getCardInfoById(id);
        PersonRepository personRepository = new PersonRepository(dataBase);
        BankRepository bankRepository = new BankRepository(dataBase);
        StringBuilder sb = new StringBuilder();
        sb.append(card)
                .append(", owner: ")
                .append(personRepository.getById(card.getOwnerId()))
                .append(", bank: ")
                .append(bankRepository.getById(card.getBankId()));
        return sb.toString().trim();
    }

    public boolean addNewEntity(String cardNumber, long bankId, long ownerId) {
        Card card = new Card(cardNumber, bankId, ownerId);
        return cardRepository.addCard(card);
    }

    public boolean deleteById(long id) {
        return cardRepository.deleteById(id);
    }
}
