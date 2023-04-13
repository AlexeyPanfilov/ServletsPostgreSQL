package ru.ap.repository;

import ru.ap.db.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BanksPersonsReqConversion {

    private DataBase dataBase = new DataBase();

    public boolean createTable() {
        dataBase.connect();
        try {
            return dataBase.getStatement().execute(
                    "CREATE TABLE IF NOT EXISTS banks_persons (" +
                            "bank_id BIGINT, " +
                            "person_id BIGINT," +
                            "foreign key (bank_id) REFERENCES banks(id) ON DELETE CASCADE," +
                            "foreign key (person_id) REFERENCES persons(id) ON DELETE CASCADE" +
                            ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
        return false;
    }

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
