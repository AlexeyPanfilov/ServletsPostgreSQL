package ru.ap.repository;

import ru.ap.db.DataBase;
import ru.ap.db.DbAccessBuilder;
import ru.ap.entities.Bank;
import ru.ap.entities.Card;
import ru.ap.entities.Person;
import ru.ap.db.DbAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BankReqConversion {

    private final DataBase dataBase;

    public BankReqConversion(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public boolean createTable() {
        dataBase.connect();
        try {
            return dataBase.getStatement().execute(
                    "CREATE TABLE IF NOT EXISTS banks (id BIGSERIAL primary key, title VARCHAR(50));"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }

    public String banksList() {
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try (ResultSet rs = dataBase.getStatement().executeQuery("SELECT * FROM banks;")) {
            while (rs.next()) {
                sb.append(rs.getLong(1)).append(" ").append(rs.getString("title")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return sb.toString().trim();
    }

    private List<Card> getCards(Bank bank) {
        List<Card> cards = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(
                    "SELECT id, number FROM cards WHERE bank_id=?;"
            );
            preparedStatement.setLong(1, bank.getId());
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

    private List<Person> getClients(Bank bank) {
        List<Person> clients = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try {
            dataBase.getConnection().setAutoCommit(false);
            PreparedStatement selectPersonsId = dataBase.getPreparedStatement(
                    "SELECT person_id FROM banks_persons WHERE bank_id=?;"
            );
            PreparedStatement selectPersonsDetails = dataBase.getPreparedStatement(
                    "SELECT name, lastname FROM persons WHERE id=?"
            );
            selectPersonsId.setLong(1, bank.getId());
            try (ResultSet getPersonIdRs = selectPersonsId.executeQuery()) {
                while (getPersonIdRs.next()) {
                    long clientId = getPersonIdRs.getLong(1);
                    selectPersonsDetails.setLong(1, clientId);
                    try (ResultSet getPersonDetailsRs = selectPersonsDetails.executeQuery()) {
                        while (getPersonDetailsRs.next()) {
                            String clientName = getPersonDetailsRs.getString(1);
                            String clientLastName = getPersonDetailsRs.getString(2);
                            Person person = new Person();
                            person.setId(clientId);
                            person.setName(clientName);
                            person.setLastName(clientLastName);
                            clients.add(person);
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
        clients.forEach(client -> sb.append(client.getFullName()).append("\n"));
        return clients;

    }

    public String cardsList() {
        Map<String, List<Card>> banksCards = new HashMap<>();
        dataBase.connect();
        try (ResultSet rs = dataBase.getStatement().executeQuery(
                "SELECT b.title, cb.number AS card FROM banks b FULL JOIN cards cb ON cb.bank_id = b.id;")
        ) {
            while (rs.next()) {
                String bankTitle = rs.getString(1);
                Bank bank = getByTitle(bankTitle);
                String cardNumber = rs.getString(2);
                if (!banksCards.containsKey(bankTitle)) {
                    List<Card> cardNumbers = new ArrayList<>();
                    Card card = new Card();
                    card.setCardNumber(cardNumber);
                    cardNumbers.add(card);
                    bank.setCards(cardNumbers);
                    banksCards.put(bankTitle, cardNumbers);
                } else {
                    List<Card> cardNumbers = banksCards.get(bankTitle);
                    Card card = new Card();
                    card.setCardNumber(cardNumber);
                    cardNumbers.add(card);
                    banksCards.put(bankTitle, cardNumbers);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        StringBuilder sb = new StringBuilder();
            banksCards.keySet().forEach(k -> banksCards.get(k)
                            .forEach(card -> sb.append(k).append(" ").append(card.getCardNumber()).append("\n")));
        return sb.toString();
    }

    public Bank getById(long id) {
        String title = "";
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement("SELECT * FROM banks WHERE id=?;");
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    id = rs.getLong(1);
                    title = rs.getString(2);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        Bank bank = new Bank();
        bank.setId(id);
        bank.setTitle(title);
        return bank;
    }

    public Bank getByTitle(String title) {
        long id = 0;
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement("SELECT * FROM banks WHERE title=?;");
            preparedStatement.setString(1, title);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    id = rs.getLong(1);
                    title = rs.getString(2);
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
            Bank bank = new Bank();
            bank.setId(id);
            bank.setTitle(title);
            bank.setCards(getCards(bank));
            bank.setPersons(getClients(bank));
            return bank;
        } else {
            return null;
        }
    }

    public boolean addBank(Bank bank) {
        dataBase.connect();
        String sql = "INSERT INTO banks (title) VALUES (?);";
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setString(1, bank.getTitle());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }

    public boolean updateBank(Bank bank) {
        dataBase.connect();
        String sql = "UPDATE banks SET title = ? WHERE id = ?;";
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setString(1, bank.getTitle());
            preparedStatement.setLong(2, bank.getId());
            preparedStatement.executeUpdate();
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
            PreparedStatement preparedStatement = dataBase.getPreparedStatement("DELETE FROM banks WHERE id=?;");
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
