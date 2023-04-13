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
        System.out.println("req.requestURI = " + requestUri);
        String[] split = requestUri.split("/");
        System.out.println("resp = " + resp.getStatus());
        PrintWriter writer = resp.getWriter();
        QueryDispatcher queryDispatcher = new QueryDispatcher();
        if (split.length == 3) {
            System.out.println("length 3, " + split[2]);
            writer.println(queryDispatcher.dispatchGetTables(split[2]));
        } else if (split.length == 4) {
            long id = -1;
            System.out.println("length 4, id = " + split[3]);
            System.out.println("path = " + split[2]);
            try {
                id = Long.parseLong(split[3]);
                writer.println(queryDispatcher.dispatchGetById(split[2], id));
            } catch (NumberFormatException e) {
                writer.println(queryDispatcher.dispatchGetByName(split[2], split[3]));
            }
        }
    }
}