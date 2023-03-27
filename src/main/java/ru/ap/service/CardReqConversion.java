package ru.ap.service;

import ru.ap.db.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CardReqConversion {

    private DataBase dataBase = new DataBase();

    public String cardsList() {
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try (ResultSet rs = dataBase.getStatement().executeQuery(
                "SELECT c.id, c.number AS card, b.title AS bank, p.name, p.lastname " +
                        "FROM cards c " +
                        "INNER JOIN banks b ON c.bank_id = b.id " +
                        "INNER JOIN persons p ON c.person_id = p.id;")) {
            while (rs.next()) {
                sb.append(rs.getLong(1))
                        .append(". ")
                        .append(rs.getString(2))
                        .append(", ")
                        .append(rs.getString(3))
                        .append(", ")
                        .append(rs.getString(4))
                        .append(" ")
                        .append(rs.getString(5))
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return sb.toString().trim();
    }

    public String getById(long id) {
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        String sql = "SELECT c.id, c.number AS card, b.title AS bank, p.name, p.lastname " +
                "FROM cards c " +
                "INNER JOIN banks b ON c.bank_id = b.id " +
                "INNER JOIN persons p ON c.person_id = p.id WHERE c.id=?" +
                "GROUP BY c.id;";
        System.out.println("sql = " + sql);
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    sb.append(rs.getLong(1))
                            .append(". ")
                            .append(rs.getString(2))
                            .append(", ")
                            .append(rs.getString(3))
                            .append(", ")
                            .append(rs.getString(4))
                            .append(" ")
                            .append(rs.getString(5))
                            .append("\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return sb.toString().trim();
    }

    public boolean addCard(String number, long bankId, long personId) {
        dataBase.connect();
        String sql = "INSERT INTO cards (number, bank_id, person_id) VALUES (?, ?, ?);";
        String sql2 = "INSERT INTO banks_persons (bank_id, person_id) VALUES (?, ?);";
        System.out.println("sql = " + sql);
        System.out.println("sql2" + sql2);
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setString(1, number);
            preparedStatement.setLong(2, bankId);
            preparedStatement.setLong(3, personId);
            preparedStatement.executeUpdate();
            PreparedStatement preparedStatement2 = dataBase.getPreparedStatement(sql2);
            preparedStatement2.setLong(1, bankId);
            preparedStatement2.setLong(2, personId);
            preparedStatement2.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }
}
