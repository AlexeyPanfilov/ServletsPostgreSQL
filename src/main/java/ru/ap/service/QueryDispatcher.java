package ru.ap.service;

import ru.ap.entities.Bank;
import ru.ap.entities.Card;
import ru.ap.entities.Person;
import ru.ap.repository.BankReqConversion;
import ru.ap.repository.BanksPersonsReqConversion;
import ru.ap.repository.CardReqConversion;
import ru.ap.repository.PersonReqConversion;

public class QueryDispatcher {

    BankReqConversion bankReqConversion = new BankReqConversion();
    CardReqConversion cardReqConversion = new CardReqConversion();
    PersonReqConversion personReqConversion = new PersonReqConversion();
    BanksPersonsReqConversion banksPersonsReqConversion = new BanksPersonsReqConversion();

    public String dispatchGetTables(String query) {
        switch (query) {
            case "banks_persons":
                return banksPersonsReqConversion.banksPersonsList();
            case "banks":
                return bankReqConversion.banksList();
            case "banks_cards":
                return bankReqConversion.cardsList();
            case "persons":
                return personReqConversion.personsList();
            case "persons_cards":
                return personReqConversion.cardsList();
            case "cards":
                StringBuilder sb = new StringBuilder();
                cardReqConversion.cardsList().keySet()
                        .forEach(id -> sb
                                .append(id)
                                .append(". ")
                                .append(cardReqConversion.cardsList().get(id).getCardNumber())
                                .append(", ")
                                .append(bankReqConversion.getById(cardReqConversion.cardsList().get(id).getBankId()).getTitle())
                                .append(", ")
                                .append(personReqConversion.getById(cardReqConversion.cardsList().get(id).getPersonId()).getFullName())
                                .append("\n"));
                return sb.toString();
            default:
                return "Invalid path";
        }
    }

    public String dispatchGetById(String table, long id) {
        switch (table) {
            case "banks":
                return bankReqConversion.getById(id).toString();
            case "banks_cards":
                return bankReqConversion.cardsList();
            case "persons":
                return personReqConversion.getById(id).toString();
            case "cards":
                Card card = cardReqConversion.getCardInfoById(id);
                return card.getCardNumber() +
                        ", " +
                        bankReqConversion.getById(card.getBankId()) +
                        ", " +
                        personReqConversion.getById(card.getPersonId());
            default:
                return "Invalid path";
        }
    }

    public String dispatchGetByName(String table, String text) {
        switch (table) {
            case "banks":
                if (bankReqConversion.getByTitle(text) != null) {
                    StringBuilder sb = new StringBuilder();
                    Bank bank = bankReqConversion.getByTitle(text);
                    sb.append("Clients:").append("\n");
                    bank.getPersons().forEach(person -> sb.append(person.getFullName()).append("\n"));
                    sb.append("Cards:").append("\n");
                    bank.getCards().forEach(card -> sb.append(card.getCardNumber()).append("\n"));
                    return sb.toString();
                } else {
                    return "no matches found";
                }
            case "banks_cards":
                return bankReqConversion.cardsList();
            case "persons":
                String[] split = text.split("_");
                String name = split[0];
                String lastName = split[1];
                if (personReqConversion.getByName(name, lastName) != null) {
                    StringBuilder sb = new StringBuilder();
                    Person person = personReqConversion.getByName(name, lastName);
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
                return bankReqConversion.addBank(bank);
            case "persons":
                Person person = new Person(query[1], query[2]);
                return personReqConversion.addPerson(person);
            case "cards":
                Card card = new Card(query[1], Long.parseLong(query[2]), Long.parseLong(query[3]));
                return cardReqConversion.addCard(card);
            case "create":
                bankReqConversion.createTable();
                personReqConversion.createTable();
                cardReqConversion.createTable();
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
                return bankReqConversion.updateBank(bankReqConversion.getById(id));
            case "persons":
                return personReqConversion.updatePerson(personReqConversion.getById(id));
            default:
                return false;
        }
    }

    public boolean dispatchDeleteById(String table, long id) {
        switch (table) {
            case "banks":
                return bankReqConversion.deleteById(id);
            case "persons":
                return personReqConversion.deleteById(id);
            default:
                return false;
        }
    }
}
