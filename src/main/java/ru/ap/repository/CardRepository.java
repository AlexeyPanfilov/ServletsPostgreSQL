package ru.ap.repository;

import ru.ap.db.DataBase;
import ru.ap.entities.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardRepository {

    private DataBase dataBase;

    public CardRepository(DataBase dataBase) {
        this.dataBase = dataBase;
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
                    card.setId(id);
                    card.setCardNumber(rs.getString(1));
                    card.setBankId(rs.getLong(2));
                    card.setOwnerId(rs.getLong(3));
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
            personIdCheckStatement.setLong(1, card.getOwnerId());
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(insertCard);
            preparedStatement.setString(1, card.getCardNumber());
            preparedStatement.setLong(2, card.getBankId());
            preparedStatement.setLong(3, card.getOwnerId());
            preparedStatement.executeUpdate();
            PreparedStatement preparedStatement2 = dataBase.getPreparedStatement(insertBanksPersons);
            preparedStatement2.setLong(1, card.getBankId());
            preparedStatement2.setLong(2, card.getOwnerId());
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

    public boolean deleteById(long id) {
        dataBase.connect();
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement("DELETE FROM cards WHERE id=?;");
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
