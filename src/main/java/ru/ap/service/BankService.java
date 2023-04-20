package ru.ap.service;

import ru.ap.db.DataBase;
import ru.ap.entities.Bank;
import ru.ap.repository.BankRepository;

public class BankService {
    private final DataBase dataBase;
    private final BankRepository bankRepository;

    public BankService(DataBase dataBase) {
        this.dataBase = dataBase;
        this.bankRepository = new BankRepository(dataBase);
    }

    public String getAllBanks() {
        return bankRepository.getBanksList();
    }

    public String getCardsById(long id) {
        return bankRepository.getCardsById(id).toString();
    }

    public String getClientsById(long id) {
        return bankRepository.getClientsById(id).toString();
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
