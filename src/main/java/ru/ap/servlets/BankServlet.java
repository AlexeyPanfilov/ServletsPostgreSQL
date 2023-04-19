package ru.ap.servlets;

import ru.ap.db.DataBase;
import ru.ap.service.BankService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class BankServlet extends HttpServlet {

    // Примеры обрабатываемых запросов
    // localhost:8080/db/bank/show/1
    // requestURI - /db/bank/show/1
    // localhost:8080/db/bank/delete/1
    // localhost:8080/db/bank/update/1
    // localhost:8080/db/bank/add/1
    BankService bankService;

    @Override
    public void init() throws ServletException {
        Object dataBase = getServletContext().getAttribute("dataBase");
        this.bankService = new BankService((DataBase) dataBase);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String[] split = requestURI.split("/");
        PrintWriter writer = resp.getWriter();
        writer.write("Valid requests:\n" +
                "/show\n" +
                "/show/?id=X\n" +
                "/show/?title=SomeTitle\n" +
                "/show/?id=X cards\n" +
                "/show/?id=X clients\n\n");
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        System.out.println("GET");
        System.out.println("requestURI: " + requestURI); // полный адрес после порта - /db/bank/show/1
        System.out.println("pathinfo: " + pathInfo); // то что после /db/bank (/show/)
        System.out.println("servletPath: " + req.getServletPath()); // /db/bank/show/
        System.out.println("contextPath: " + req.getContextPath());
        System.out.println("queryString: " + req.getQueryString());
        System.out.println("parameter: " + req.getParameter("id")); // /db/bank/show/?id=5 (выдаст 5)
        if (pathInfo.equals("show")) {
            if (queryString == null) {
                writer.write(bankService.getBanks());
            } else {
                String byId = req.getParameter("id");
                String title = req.getParameter("title");
                long id;
                if (byId != null) {
                    try {
                        id = Long.parseLong(byId);
                        writer.write(bankService.getById(id));
                    } catch (NumberFormatException e) {
                        String[] splitParam = req.getParameter("id").split(" ");
                        id = Long.parseLong(splitParam[0]);
                        switch (splitParam[1]) {
                            case "cards":
                                writer.write(bankService.getCards(id));
                                break;
                            case "clients":
                                writer.write(bankService.getClients(id));
                                break;
                            default:
                                writer.write("unknown parameter");
                                break;
                        }
                    }
                }
                if (title != null) {
                    title = req.getParameter("title");
                    writer.write(bankService.getByTitle(title));
                }
            }
        } else {
            writer.write("Unknown operation");
        }
    }

    // add
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        System.out.println("POST");
        if (queryString == null) {
            writer.write("request needed");
        } else if (pathInfo.equals("add")) {
            String title = req.getParameter("title");
            if (title != null) {
                title = req.getParameter("title");
                boolean add = bankService.addNewEntity(title);
                if (add) {
                    writer.write("Entity added");
                } else {
                    writer.write("Unable to add new entity");
                }
            }
        } else {
            writer.write("Unknown operation");
        }
    }

    // update
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        PrintWriter writer = resp.getWriter();
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        System.out.println("PUT");
        System.out.println("requestURI: " + requestURI); // полный адрес после порта - /db/bank/show/1
        System.out.println("pathinfo: " + req.getPathInfo()); // то что после /db/bank/show/
        System.out.println("servletPath: " + req.getServletPath()); // /db/bank/show/
        System.out.println("contextPath: " + req.getContextPath());
        System.out.println("queryString: " + req.getQueryString()); // то, что после /db/bank/show/? (после вопроса)
        if (queryString == null) {
            writer.write("request needed");
        } else if (pathInfo.equals("update")) {
            Map<String, String[]> parameterMap = req.getParameterMap();
            String byId = parameterMap.get("id")[0];
            String title = parameterMap.get("title")[0];
            long id;
            if (byId != null && title != null) {
                try {
                    id = Long.parseLong(byId);
                    boolean update = bankService.updateById(id, title);
                    if (update) {
                        writer.write("Update successful");
                    } else {
                        writer.write("Update failed");
                    }
                } catch (NumberFormatException e) {
                    writer.write("Invalid id");
                }
            }
        } else {
            writer.write("Unknown operation");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.write("Valid requests:\n" +
                "/show\n" +
                "/show/?id=X\n" +
                "/show/?title=SomeTitle\n\n");
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        System.out.println("DELETE");
        if (queryString == null) {
            writer.write("List of banks:\n");
            writer.write(bankService.getBanks());
        } else if (pathInfo.equals("delete")) {
            String byId = req.getParameter("id");
            long id;
            if (byId != null) {
                try {
                    id = Long.parseLong(byId);
                    boolean isDeleted = bankService.deleteById(id);
                    if (isDeleted) {
                        writer.write("Delete successful");
                    } else {
                        writer.write("Unable to delete");
                    }
                } catch (NumberFormatException e) {
                    writer.write("Invalid id");
                }
            }
        } else {
            writer.write("Unknown operation");
        }
    }
}
