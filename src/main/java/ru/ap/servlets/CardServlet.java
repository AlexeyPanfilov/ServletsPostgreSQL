package ru.ap.servlets;

import ru.ap.db.DataBase;
import ru.ap.service.CardService;
import ru.ap.service.PersonService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CardServlet extends HttpServlet {

    private CardService cardService;

    @Override
    public void init() throws ServletException {
        Object dataBase = getServletContext().getAttribute("dataBase");
        this.cardService = new CardService((DataBase) dataBase);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validGetRequests = "Valid requests:\n" +
                "/show/?id=x\n";
        PrintWriter writer = resp.getWriter();
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        if (pathInfo.equals("show")) {
            if (queryString == null) {
                writer.write(validGetRequests);
            } else {
                String byId = req.getParameter("id");
                long id;
                if (byId != null && !byId.isBlank()) {
                    try {
                        id = Long.parseLong(byId);
                        writer.write(cardService.getCardInfoById(id));
                    } catch (NumberFormatException e) {
                        writer.write("Invalid id\n" + validGetRequests);
                    }
                } else {
                    writer.write("Invalid request\n" + validGetRequests);
                }
            }
        } else {
            writer.write("Invalid id\n" + validGetRequests);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validPostRequests = "Valid requests:\n" +
                "/add/?number=SomeNumber&bank_id=x&owner_id=y\n";
        PrintWriter writer = resp.getWriter();
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        if (queryString == null) {
            writer.write("Request needed\n" + validPostRequests);
        } else if (pathInfo.equals("add")) {
            Map<String, String[]> parameterMap = req.getParameterMap();
            if (parameterMap != null && !parameterMap.isEmpty()) {
                try {
                    String cardNumber = parameterMap.get("number")[0];
                    String bankIdStr = parameterMap.get("bank_id")[0];
                    String ownerIdStr = parameterMap.get("owner_id")[0];
                    boolean add = false;
                    try {
                        long bankId = Long.parseLong(bankIdStr);
                        long ownerId = Long.parseLong(ownerIdStr);
                        add = cardService.addNewEntity(cardNumber, bankId, ownerId);
                    } catch (NumberFormatException e) {
                        writer.write("Invalid input\n" + validPostRequests);
                    }
                    if (add) {
                        writer.write("Entity added");
                    } else {
                        writer.write("Unable to add new entity");
                    }
                } catch (NullPointerException e) {
                    writer.write("Invalid input\n" + validPostRequests);
                }
            }
        } else {
            writer.write("Invalid request\n" + validPostRequests);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String validDeleteRequests = "Valid requests:\n" +
                "/delete/?id=X\n";
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        if (queryString == null && pathInfo.equals("delete")) {
            writer.write("List of banks:\n");
        } else if (pathInfo.equals("delete")) {
            String byId = req.getParameter("id");
            long id;
            if (byId != null) {
                try {
                    id = Long.parseLong(byId);
                    boolean isDeleted = cardService.deleteById(id);
                    if (isDeleted) {
                        writer.write("Delete successful");
                    } else {
                        writer.write("Unable to delete");
                    }
                } catch (NumberFormatException e) {
                    writer.write("Invalid id\n" + validDeleteRequests);
                }
            }
        } else {
            writer.write("Invalid request\n" + validDeleteRequests);
        }
    }
}
