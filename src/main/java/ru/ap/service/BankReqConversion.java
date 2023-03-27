package ru.ap.service;

import ru.ap.db.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankReqConversion {

    private DataBase dataBase = new DataBase();

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

    public String cardsList() {
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try (ResultSet rs = dataBase.getStatement().executeQuery(
                "SELECT b.title, cb.number AS card FROM banks b FULL JOIN cards cb ON cb.bank_id = b.id;")
        ) {
            while (rs.next()) {
                sb.append(rs.getString(1))
                        .append(": ")
                        .append(rs.getString(2))
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
        String sql = "SELECT * FROM banks WHERE id=?;";
        System.out.println("sql = " + sql);
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    sb.append(rs.getLong(1))
                            .append(" ")
                            .append(rs.getString(2))
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

    public boolean addBank(String title) {
        dataBase.connect();
        String sql = "INSERT INTO banks (title) VALUES (?);";
        System.out.println("sql = " + sql);
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }

    public boolean updateBank(String title, long id) {
        dataBase.connect();
        String sql = "UPDATE banks SET title = ? WHERE id = ?;";
        System.out.println("sql = " + sql);
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setLong(2, id);
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
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        String sql = "DELETE FROM banks WHERE id=?;";
        System.out.println("sql = " + sql);
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
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
