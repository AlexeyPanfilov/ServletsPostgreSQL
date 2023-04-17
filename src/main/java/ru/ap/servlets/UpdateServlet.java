package ru.ap.servlets;

import ru.ap.service.QueryDispatcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/update.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String table = req.getParameter("table");
        String id = req.getParameter("id");
        String column1 = req.getParameter("column1");
        String column2 = req.getParameter("column2");
        String column3 = req.getParameter("column3");
//        System.out.println("id = " + id);
//        System.out.println("table = " + table);
//        System.out.println("column1 = " + column1);
//        System.out.println("column2 = " + column2);
//        System.out.println("column3 = " + column3);
        String[] query = {table, id, column1, column2, column3};
        QueryDispatcher queryDispatcher = new QueryDispatcher(
                DbAccessData.DB_CLASS_NAME,
                DbAccessData.DB_URL,
                DbAccessData.DB_USER,
                DbAccessData.DB_PASSWORD
        );
        queryDispatcher.dispatchUpdateById(query);
        doGet(req, resp);
    }
}
