package ru.ap.servlets;

import ru.ap.db.DataBase;
import ru.ap.service.BankReqConversion;
import ru.ap.service.PersonReqConversion;
import ru.ap.service.QueryDispatcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class DbWorker extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/db.jsp");
        dispatcher.forward(req, resp);
//        String queryString = req.getQueryString();
//        System.out.println("queryString = " + queryString);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sql = req.getQueryString();
        System.out.println(sql);
    }
}
