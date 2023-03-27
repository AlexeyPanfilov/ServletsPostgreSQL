package ru.ap;

import ru.ap.db.DataBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class PrepareTables {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        dataBase.connect();
        prepareData(dataBase);
        dataBase.disconnect();
    }

    static void prepareData(DataBase dataBase) {
        try {
            String sql = Files.lines(Paths.get("create-tables.sql")).collect(Collectors.joining(" "));
            dataBase.getStatement().execute(sql);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
