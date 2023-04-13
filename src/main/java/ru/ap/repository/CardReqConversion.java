package ru.ap.repository;

import ru.ap.db.DataBase;
import ru.ap.entities.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CardReqConversion {

    private DataBase dataBase = new DataBase();

    public boolean createTable() {
        dataBase.connect();
        try {
            return dataBase.getStatement().execute(
                            "CREATE TABLE cards (" +
                                    "id BIGSERIAL primary key,number VARCHAR(9)," +
                                    "bank_id BIGINT REFERENCES banks (id) ON DELETE CASCADE, " +
                                    "person_id BIGINT REFERENCES persons (id) ON DELETE CASCADE)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }

    public Map<Long, Card> cardsList() {
        Map<Long, Card> cards = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try (
        ResultSet rs1 = dataBase.getStatement().executeQuery("SELECT * FROM cards")) {
            while (rs1.next()) {
                long cardId = rs1.getLong(1);
                String cardNumber = rs1.getString(2);
                long bankId = rs1.getLong(3);
                long personId = rs1.getLong(4);
                cards.put(cardId, new Card(cardNumber, bankId, personId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return cards;
    }

    public Card getCardInfoById(long id) {
        Card card = new Card();
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(
                    "SELECT number, bank_id, person_id FROM cards WHERE id=?"
            );
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    card.setCardNumber(rs.getString(1));
                    card.setBankId(rs.getLong(2));
                    card.setPersonId(rs.getLong(3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    public boolean addCard(Card card) {
        String bankIdCheck = "SELECT * FROM banks WHERE id = ?";
        String personIdCheck = "SELECT * FROM persons WHERE id = ?";
        String insertCard = "INSERT INTO cards (number, bank_id, person_id) VALUES (?, ?, ?);";
        String insertBanksPersons = "INSERT INTO banks_persons (bank_id, person_id) VALUES (?, ?);";
        dataBase.connect();
        try {
            dataBase.getConnection().setAutoCommit(false);
            PreparedStatement bankIdCheckStatement = dataBase.getPreparedStatement(bankIdCheck);
            bankIdCheckStatement.setLong(1, card.getBankId());
            PreparedStatement personIdCheckStatement = dataBase.getPreparedStatement(personIdCheck);
            personIdCheckStatement.setLong(1, card.getPersonId());
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(insertCard);
            preparedStatement.setString(1, card.getCardNumber());
            preparedStatement.setLong(2, card.getBankId());
            preparedStatement.setLong(3, card.getPersonId());
            preparedStatement.executeUpdate();
            PreparedStatement preparedStatement2 = dataBase.getPreparedStatement(insertBanksPersons);
            preparedStatement2.setLong(1, card.getBankId());
            preparedStatement2.setLong(2, card.getPersonId());
            preparedStatement2.executeUpdate();
            dataBase.getConnection().commit();
            dataBase.getConnection().setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try {
                dataBase.getConnection().rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }
}
