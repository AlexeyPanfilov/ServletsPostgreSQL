package ru.ap.servlets;

import ru.ap.service.QueryDispatcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/delete.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String table = req.getParameter("table");
        String idStr = req.getParameter("id");
        System.out.println("id = " + idStr);
        System.out.println("table = " + table);
        long id = Long.parseLong(idStr);
        QueryDispatcher queryDispatcher = new QueryDispatcher();
        System.out.println(queryDispatcher.dispatchDelete(table, id));
        doGet(req, resp);
    }
}
