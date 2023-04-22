package ru.ap.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.stream.Collectors;

public class DataBase {

    private static Connection connection;
    private Statement statement;
    private String className;
    private String url;
    private String user;
    private String password;

    public DataBase(String className, String url, String user, String password) {
        this.className = className;
        this.url = url;
        this.user = user;
        this.password = password;
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        createTablesIfNotExists();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Unable to connect database");
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
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

//    private void createTablesIfNotExists() {
//        connect();
//        try {
//            String sql = Files.lines(Paths.get("create-and-fill-test-tables.sql")).collect(Collectors.joining(" "));
//            statement.execute(sql);
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//        disconnect();
//    }
}
