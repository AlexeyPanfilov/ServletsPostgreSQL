package ru.ap.listener;

import ru.ap.db.DataBase;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {

    DataBase dataBase;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
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

        createTablesIfNotExists(servletContext);
    }

    private void createTablesIfNotExists(ServletContext servletContext) {
        try (InputStream inputStream = servletContext.getResourceAsStream("WEB-INF/resources/create-tables.sql")) {
            if (inputStream == null) return;
            dataBase.connect();
            String sql = new String(inputStream.readAllBytes());
            dataBase.getStatement().execute(sql);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            dataBase.disconnect();
        }
    }
}