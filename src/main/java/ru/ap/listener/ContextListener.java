package ru.ap.listener;

import ru.ap.db.DataBase;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();
        DataBase dataBase = null;

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
    }
}