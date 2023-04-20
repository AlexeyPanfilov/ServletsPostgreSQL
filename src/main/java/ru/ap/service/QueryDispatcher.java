package ru.ap.service;

import ru.ap.db.DataBase;
import ru.ap.entities.Bank;
import ru.ap.entities.Card;
import ru.ap.entities.Person;
import ru.ap.repository.BankRepository;
import ru.ap.repository.BanksPersonsReqConversion;
import ru.ap.repository.CardRepository;
import ru.ap.repository.PersonRepository;

public class QueryDispatcher {

    DataBase dataBase;
    BankRepository bankRepository;
    CardRepository cardRepository;
    PersonRepository personRepository;
    BanksPersonsReqConversion banksPersonsReqConversion;

    public QueryDispatcher(String dbClassName, String dbUrl, String dbUser, String dbPassword) {
        this.dataBase = new DataBase(dbClassName, dbUrl, dbUser, dbPassword);
        bankRepository = new BankRepository(dataBase);
        cardRepository = new CardRepository(dataBase);
        personRepository = new PersonRepository(dataBase);
        banksPersonsReqConversion = new BanksPersonsReqConversion(dataBase);
    }

    public DataBase getDataBase() {
        return this.dataBase;
    }

    public String dispatchGetTables(String query) {
        switch (query) {
            case "banks_persons":
                return banksPersonsReqConversion.banksPersonsList();
            case "banks":
                return bankRepository.getBanksList();
            case "banks_cards":
                return bankRepository.cardsList();
            case "persons":
                return personRepository.getPersonsList();
            case "persons_cards":
                return personRepository.cardsList();
            case "cards":
                StringBuilder sb = new StringBuilder();
                cardRepository.cardsList().keySet()
                        .forEach(id -> sb
                                .append(id)
                                .append(". ")
                                .append(cardRepository.cardsList().get(id).getCardNumber())
                                .append(", ")
                                .append(bankRepository.getById(cardRepository.cardsList().get(id).getBankId()).getTitle())
                                .append(", ")
                                .append(personRepository.getById(cardRepository.cardsList().get(id).getOwnerId()).getFullName())
                                .append("\n"));
                return sb.toString().trim();
            default:
                return "Invalid path";
        }
    }

    public String dispatchGetById(String table, long id) {
        switch (table) {
            case "banks":
                return bankRepository.getById(id).toString();
            case "banks_cards":
                return bankRepository.cardsList();
            case "persons":
                return personRepository.getById(id).toString();
            case "cards":
                Card card = cardRepository.getCardInfoById(id);
                return card.getCardNumber() +
                        ", " +
                        bankRepository.getById(card.getBankId()) +
                        ", " +
                        personRepository.getById(card.getOwnerId());
            default:
                return "Invalid path";
        }
    }

    public String dispatchGetByName(String table, String text) {
        switch (table) {
            case "banks":
                if (bankRepository.getByTitle(text) != null) {
                    StringBuilder sb = new StringBuilder();
                    Bank bank = bankRepository.getByTitle(text);
                    sb.append("Clients:").append("\n");
                    bank.getPersons().forEach(person -> sb.append(person.getFullName()).append("\n"));
                    sb.append("Cards:").append("\n");
                    bank.getCards().forEach(card -> sb.append(card.getCardNumber()).append("\n"));
                    return sb.toString();
                } else {
                    return "no matches found";
                }
            case "banks_cards":
                return bankRepository.cardsList();
            case "persons":
                String[] split = text.split("_");
                String name = split[0];
                String lastName = split[1];
                if (personRepository.getByFullName(name, lastName) != null) {
                    StringBuilder sb = new StringBuilder();
                    Person person = personRepository.getByFullName(name, lastName);
                    sb.append("Banks:").append("\n");
                    person.getBanks().forEach(bank -> sb.append(bank.getTitle()).append("\n"));
                    sb.append("Cards:").append("\n");
                    person.getCards().forEach(card -> sb.append(card.getCardNumber()).append("\n"));
                    return sb.toString();
                } else {
                    return "no matches found";
                }
//            case "cards":
//                return cardReqConversion.getById(id);
        }
        return "No match found";
    }


    public boolean dispatchAdd(String[] query) {
        switch (query[0]) {
            case "banks":
                Bank bank = new Bank(query[1]);
                return bankRepository.addNewEntity(bank);
            case "persons":
                Person person = new Person(query[1], query[2]);
                return personRepository.addNewEntity(person);
            case "cards":
                Card card = new Card(query[1], Long.parseLong(query[2]), Long.parseLong(query[3]));
                return cardRepository.addCard(card);
            case "create":
                bankRepository.createTable();
                personRepository.createTable();
                cardRepository.createTable();
                banksPersonsReqConversion.createTable();
                return true;
            default:
                return false;
        }
    }

    public boolean dispatchUpdateById(String[] query) {
        long id = Long.parseLong(query[1]);
        switch (query[0]) {
            case "banks":
                return bankRepository.updateById(id, query[2]);
            case "persons":
//                return personRepository.updateById(personRepository.getById(id), query[2], query[3]);
            default:
                return false;
        }
    }

    public boolean dispatchDeleteById(String table, long id) {
        switch (table) {
            case "banks":
                return bankRepository.deleteById(id);
            case "persons":
                return personRepository.deleteById(id);
            case "cards":
                return cardRepository.deleteById(id);
            default:
                return false;
        }
    }
}
