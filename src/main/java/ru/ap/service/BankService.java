package ru.ap.service;

import ru.ap.db.DataBase;
import ru.ap.entities.Bank;
import ru.ap.listener.ContextListener;
import ru.ap.repository.BankRepository;

public class BankService {
    DataBase dataBase;
    BankRepository bankRepository;

    public BankService(DataBase dataBase) {
//        this.dataBase = new DataBase(
//                "org.postgresql.Driver",
//                "jdbc:postgresql://localhost:5432/?currentSchema=aston3",
//                "postgres",
//                "admin"
//        );
        this.dataBase = dataBase;
        this.bankRepository = new BankRepository(dataBase);
    }

    public String getBanks() {
        return bankRepository.getBanksList();
    }

    public String getCards(long id) {
        return bankRepository.getCards(id).toString();
    }

    public String getClients(long id) {
        return bankRepository.getClients(id).toString();
    }

    public String getById(long id) {
        return bankRepository.getById(id).toString();
    }

    public String getByTitle(String title) {
        return bankRepository.getByTitle(title).toString();
    }

    public boolean addNewEntity(String title) {
        Bank bank = new Bank(title);
        return bankRepository.addNewEntity(bank);
    }

    public boolean updateById(long id, String title) {
        return bankRepository.updateById(id, title);
    }

    public boolean deleteById(long id) {
        return bankRepository.deleteById(id);
    }
}
