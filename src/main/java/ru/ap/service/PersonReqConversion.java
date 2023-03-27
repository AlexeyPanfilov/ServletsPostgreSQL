package ru.ap.service;

import ru.ap.db.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonReqConversion {

    private DataBase dataBase = new DataBase();

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

    public String getById(long id) {
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        String sql = "SELECT * FROM persons WHERE id=?;";
        System.out.println("sql = " + sql);
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return sb.toString().trim();
    }

    public boolean addPerson(String name, String lastName) {
        dataBase.connect();
        String sql = "INSERT INTO persons (name, lastname) VALUES (?, ?);";
        System.out.println("sql = " + sql);
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }

    public boolean updatePerson(String name, String lastName, long id) {
        dataBase.connect();
        String sql = "UPDATE persons SET name = ?, lastname = ? WHERE id = ?;";
        System.out.println("sql = " + sql);
        try {
            PreparedStatement preparedStatement = dataBase.getPreparedStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setLong(3, id);
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
        String sql = "DELETE FROM persons WHERE id=?;";
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
