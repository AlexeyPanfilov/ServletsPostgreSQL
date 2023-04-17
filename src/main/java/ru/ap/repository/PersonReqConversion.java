package ru.ap.repository;

import ru.ap.db.DataBase;
import ru.ap.entities.Bank;
import ru.ap.entities.Card;
import ru.ap.entities.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonReqConversion {

    private DataBase dataBase;

    public PersonReqConversion(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public boolean createTable() {
        dataBase.connect();
        try {
            return dataBase.getStatement().execute(
                    "CREATE TABLE IF NOT EXISTS persons (" +
                            "id BIGSERIAL primary key, name VARCHAR(50), lastname VARCHAR(50)" +
                            ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }

    public String personsList() {
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try (ResultSet rs = dataBase.getStatement().executeQuery("SELECT * FROM persons;")) {
            while (rs.next()) {
                sb.append(rs.getLong(1))
                        .append(" ")
                        .append(rs.getString(2))
                        .append(" ")
                        .append(rs.getString(3))
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return sb.toString().trim();
    }

    private List<Card> getCards(Person person) {
        List<Card> cards = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(
                    "SELECT id, number FROM cards WHERE person_id=?;"
            );
            preparedStatement.setLong(1, person.getId());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    long cardId = rs.getLong(1);
                    String cardNumber = rs.getString(2);
                    Card card = new Card();
                    card.setId(cardId);
                    card.setCardNumber(cardNumber);
                    cards.add(card);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        cards.forEach(card -> sb.append(card.getCardNumber()).append("\n"));
        return cards;
    }

    private List<Bank> getBanks(Person person) {
        List<Bank> banks = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try {
            dataBase.getConnection().setAutoCommit(false);
            PreparedStatement selectBanksId = dataBase.getPreparedStatement(
                    "SELECT bank_id FROM banks_persons WHERE person_id=?;"
            );
            PreparedStatement selectBanksDetails = dataBase.getPreparedStatement(
                    "SELECT title FROM banks WHERE id=?"
            );
            selectBanksId.setLong(1, person.getId());
            try (ResultSet getBankIdRs = selectBanksId.executeQuery()) {
                while (getBankIdRs.next()) {
                    long bankId = getBankIdRs.getLong(1);
                    selectBanksDetails.setLong(1, bankId);
                    try (ResultSet getBankDetailsRs = selectBanksDetails.executeQuery()) {
                        while (getBankDetailsRs.next()) {
                            String title = getBankDetailsRs.getString(1);
                            Bank bank = new Bank();
                            bank.setId(bankId);
                            bank.setTitle(title);
                            banks.add(bank);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        banks.forEach(bank -> sb.append(bank.getTitle()).append("\n"));
        return banks;

    }

    public String cardsList() {
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try (ResultSet rs = dataBase.getStatement().executeQuery(
                "SELECT p.name, p.lastname, cp.number AS card " +
                        "FROM persons p FULL JOIN cards cp ON cp.person_id = p.id;"
        )) {
            while (rs.next()) {
                sb.append(rs.getString(1))
                        .append(" ")
                        .append(rs.getString(2))
                        .append(" ")
                        .append(rs.getString(3))
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return sb.toString().trim();
    }

    public Person getById(long id) {
        String name = "";
        String lastName = "";
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(
                    "SELECT * FROM persons WHERE id=?;"
            );
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString(2);
                    lastName = rs.getString(3);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setLastName(lastName);
        return person;
    }

    public Person getByName(String name, String lastName) {
        long id = 0;
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(
                    "SELECT * FROM persons WHERE (name, lastname)=(?, ?);"
            );
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    id = rs.getLong(1);
                    name = rs.getString(2);
                    lastName = rs.getString(3);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        if (id > 0) {
            Person person = new Person();
            person.setId(id);
            person.setName(name);
            person.setLastName(lastName);
            person.setBanks(getBanks(person));
            person.setCards(getCards(person));
            return person;
        } else {
            return null;
        }
    }

    public boolean addPerson(Person person) {
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(
                    "INSERT INTO persons (name, lastname) VALUES (?, ?);"
            );
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }

    public boolean updatePerson(Person person, String newName, String newLastName) {
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(
                    "UPDATE persons SET name = ?, lastname = ? WHERE id = ?;"
            );
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newLastName);
            preparedStatement.setLong(3, person.getId());
            preparedStatement.executeUpdate();
            person.setName(newName);
            person.setLastName(newLastName);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }

    public boolean deleteById(long id) {
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement("DELETE FROM persons WHERE id=?;");
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }
}
