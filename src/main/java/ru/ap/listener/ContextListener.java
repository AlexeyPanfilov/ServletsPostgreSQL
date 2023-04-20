package ru.ap.listener;

import ru.ap.db.DataBase;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {

    DataBase dataBase = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        try (InputStream inputStream = servletContext.getResourceAsStream("WEB-INF/resources/db.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            dataBase = new DataBase(
                    properties.getProperty("db.className"),
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        servletContext.setAttribute("dataBase", dataBase);

        createTables();
    }

    private void createTables() {
        dataBase.connect();
        try {
            dataBase.getStatement().execute(
                    "CREATE TABLE IF NOT EXISTS persons (" +
                            "id BIGSERIAL primary key, name VARCHAR(50), lastname VARCHAR(50)" +
                            ");"
            );
            dataBase.getStatement().execute(
                    "CREATE TABLE IF NOT EXISTS banks (id BIGSERIAL primary key, title VARCHAR(50));"
            );
            dataBase.getStatement().execute(
                    "CREATE TABLE IF NOT EXISTS cards (" +
                            "id BIGSERIAL primary key,number VARCHAR(9)," +
                            "bank_id BIGINT REFERENCES banks (id) ON DELETE CASCADE, " +
                            "person_id BIGINT REFERENCES persons (id) ON DELETE CASCADE)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
    }
}