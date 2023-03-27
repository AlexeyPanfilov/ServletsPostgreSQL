package ru.ap.service;

public class QueryDispatcher {

    BankReqConversion bankReqConversion = new BankReqConversion();
    CardReqConversion cardReqConversion = new CardReqConversion();
    PersonReqConversion personReqConversion = new PersonReqConversion();
    BanksPersonsReqConversion banksPersonsReqConversion = new BanksPersonsReqConversion();

    public String dispatchTables(String query) {
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
                return cardReqConversion.cardsList();
        }
        return "Invalid path";
    }

    public String dispatchById(String table, long id) {
        switch (table) {
            case "banks":
                return bankReqConversion.getById(id);
            case "banks_cards":
                return bankReqConversion.cardsList();
            case "persons":
                return personReqConversion.getById(id);
            case "cards":
                return cardReqConversion.getById(id);
        }
        return "Invalid path";
    }

    public boolean dispatchAdd(String[] query) {
        switch (query[0]) {
            case "banks":
                return bankReqConversion.addBank(query[1]);
            case "persons":
                return personReqConversion.addPerson(query[1], query[2]);
            case "cards":
                return cardReqConversion.addCard(query[1], Long.parseLong(query[2]), Long.parseLong(query[3]));
        }
        return false;
    }

    public boolean dispatchUpdate(String[] query) {
        long id = Long.parseLong(query[1]);
        switch (query[0]) {
            case "banks":
                return bankReqConversion.updateBank(query[2], id);
            case "persons":
                return personReqConversion.updatePerson(query[2], query[3], id);
        }
        return false;
    }

    public boolean dispatchDelete(String table, long id) {
        switch (table) {
            case "banks":
                return bankReqConversion.deleteById(id);
            case "persons":
                return personReqConversion.deleteById(id);
        }
        return false;
    }
}
