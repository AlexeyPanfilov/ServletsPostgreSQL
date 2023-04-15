package ru.ap.servlets;

import ru.ap.service.QueryDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ReadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        String[] split = requestUri.split("/");
        PrintWriter writer = resp.getWriter();
        QueryDispatcher queryDispatcher = new QueryDispatcher(
                DbAccessData.DB_CLASS_NAME,
                DbAccessData.DB_URL,
                DbAccessData.DB_USER,
                DbAccessData.DB_PASSWORD
        );
        if (split.length == 3) {
            writer.println(queryDispatcher.dispatchGetTables(split[2]));
        } else if (split.length == 4) {
            try {
                long id = Long.parseLong(split[3]);
                writer.println(queryDispatcher.dispatchGetById(split[2], id));
            } catch (NumberFormatException e) {
                writer.println(queryDispatcher.dispatchGetByName(split[2], split[3]));
            }
        }
    }
}