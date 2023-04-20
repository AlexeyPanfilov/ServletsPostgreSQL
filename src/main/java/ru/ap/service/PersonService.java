package ru.ap.service;

import ru.ap.db.DataBase;
import ru.ap.entities.Person;
import ru.ap.repository.PersonRepository;

public class PersonService {

    private final DataBase dataBase;
    private final PersonRepository personRepository;

    public PersonService(DataBase dataBase) {
        this.dataBase = dataBase;
        this.personRepository = new PersonRepository(dataBase);
    }

    public String getAllPersons() {
        return personRepository.getPersonsList();
    }

    public String getBanksById(long id) {
        StringBuilder sb = new StringBuilder();
        personRepository.getBanksById(id).forEach(bank -> sb.append(bank).append("\n"));
        return sb.toString().trim();
    }

    public String getCardsById(long id) {
        StringBuilder sb = new StringBuilder();
        personRepository.getCardsById(id).forEach(card -> sb.append(card).append("\n"));
        return sb.toString().trim();
    }

    public String getById(long id) {
        return personRepository.getById(id).toString();
    }

    public String getByFullName(String name, String lastName) {
        return personRepository.getByFullName(name, lastName).toString();
    }

    public boolean addNewEntity(String name, String lastName) {
        Person person = new Person(name, lastName);
        return personRepository.addNewEntity(person);
    }

    public boolean updateById(long id, String newName, String newLastName) {
        return personRepository.updateById(id, newName, newLastName);
    }

    public boolean deleteById(long id) {
        return personRepository.deleteById(id);
    }
}
