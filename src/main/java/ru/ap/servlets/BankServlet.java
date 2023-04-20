package ru.ap.servlets;

import ru.ap.db.DataBase;
import ru.ap.service.BankService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class BankServlet extends HttpServlet {

    private BankService bankService;

    @Override
    public void init() throws ServletException {
        Object dataBase = getServletContext().getAttribute("dataBase");
        this.bankService = new BankService((DataBase) dataBase);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validGetRequests = "Valid requests:\n" +
                "/show\n" +
                "/show/?id=X\n" +
                "/show/?title=SomeTitle\n" +
                "/show/?id=X cards\n" +
                "/show/?id=X clients\n\n";
        PrintWriter writer = resp.getWriter();
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        if (pathInfo.equals("show")) {
            if (queryString == null) {
                writer.write(bankService.getAllBanks());
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
                                writer.write(bankService.getCardsById(id));
                                break;
                            case "clients":
                                writer.write(bankService.getClientsById(id));
                                break;
                            default:
                                writer.write("Unknown parameter\n" + validGetRequests);
                                break;
                        }
                    }
                } else if (title != null) {
                    title = req.getParameter("title");
                    writer.write(bankService.getByTitle(title));
                } else {
                    writer.write("Invalid request\n" + validGetRequests);
                }
            }
        } else {
            writer.write("Invalid request\n" + validGetRequests);
        }
    }

    // add
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validPostRequests = "Valid requests:\n" +
                "/add\n" +
                "/add/?title=SomeTitle\n";
        PrintWriter writer = resp.getWriter();
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        System.out.println("POST");
        if (queryString == null) {
            writer.write("Request needed\n" + validPostRequests);
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
            writer.write("Invalid request\n" + validPostRequests);
        }
    }

    // update
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validPutRequests = "Valid requests:\n" +
                "/edit/?id=x&title=SomeTitle\n";
        PrintWriter writer = resp.getWriter();
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        if (queryString == null) {
            writer.write("request needed");
        } else if (pathInfo.equals("edit")) {
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
                    writer.write("Invalid id\n" + validPutRequests);
                }
            }
        } else {
            writer.write("Invalid request\n" + validPutRequests);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String validDeleteRequests = "Valid requests:\n" +
                "/delete/?id=X\n";
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        if (queryString == null) {
            writer.write("List of banks:\n");
            writer.write(bankService.getAllBanks());
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
                    writer.write("Invalid id\n" + validDeleteRequests);
                }
            }
        } else {
            writer.write("Invalid request\n" + validDeleteRequests);
        }
    }
}
