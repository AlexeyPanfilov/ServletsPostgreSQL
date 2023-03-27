package ru.ap.db;

import java.sql.*;

public class DataBase {

    private static Connection connection;

    private Statement statement;

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/?currentSchema=aston3",
                    "postgres",
                    "admin"
            );
            statement = connection.createStatement();
            System.out.println("Connected to DB");
        } catch (SQLException e) {
            System.out.println("Unable to connect database");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
            System.out.println("Disconnected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResultSet() throws SQLException {
        return statement.getResultSet();
    }

    public Statement getStatement() {
        return statement;
    }

    public PreparedStatement getPreparedStatement(String value) throws SQLException {
        return connection.prepareStatement(value);
    }
}
