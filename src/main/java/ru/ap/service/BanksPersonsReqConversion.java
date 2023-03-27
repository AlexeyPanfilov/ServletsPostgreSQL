package ru.ap.service;

import ru.ap.db.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BanksPersonsReqConversion {

    private DataBase dataBase = new DataBase();

    public String banksPersonsList() {
        StringBuilder sb = new StringBuilder();
        dataBase.connect();
        try (
                ResultSet rs = dataBase.getStatement().executeQuery(
                        "SELECT p.name, p.lastName, b.title AS bank" +
                                " FROM persons p" +
                                " RIGHT JOIN banks_persons pb ON pb.person_id = p.id" +
                                " RIGHT JOIN banks b ON b.id = pb.bank_id;"
                )
        ) {
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
}
