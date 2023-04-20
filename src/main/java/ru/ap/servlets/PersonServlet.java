package ru.ap.servlets;

import ru.ap.db.DataBase;
import ru.ap.service.PersonService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class PersonServlet extends HttpServlet {

    private PersonService personService;

    @Override
    public void init() throws ServletException {
        Object dataBase = getServletContext().getAttribute("dataBase");
        this.personService = new PersonService((DataBase) dataBase);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validGetRequests = "Valid requests:\n" +
                "/show\n" +
                "/show/?id=x\n" +
                "/show/?name=SomeName&lastname=SomeLastName\n" +
                "/show/?id=x cards\n" +
                "/show/?id=x banks\n\n";
        PrintWriter writer = resp.getWriter();
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        if (pathInfo.equals("show")) {
            if (queryString == null) {
                writer.write(personService.getAllPersons());
            } else {
                String byId = req.getParameter("id");
                Map<String, String[]> parameterMap = req.getParameterMap();
                long id;
                if (byId != null && !byId.isBlank()) {
                    try {
                        id = Long.parseLong(byId);
                        writer.write(personService.getById(id));
                    } catch (NumberFormatException e) {
                        String[] splitParam = req.getParameter("id").split(" ");
                        id = Long.parseLong(splitParam[0]);
                        switch (splitParam[1]) {
                            case "cards":
                                writer.write(personService.getCardsById(id));
                                break;
                            case "clients":
                                writer.write(personService.getBanksById(id));
                                break;
                            default:
                                writer.write("unknown parameter\n" + validGetRequests);
                                break;
                        }
                    }
                } else if (parameterMap != null && !parameterMap.isEmpty()) {
                    try {
                        String name = parameterMap.get("name")[0];
                        String lastName = parameterMap.get("lastname")[0];
                        writer.write(personService.getByFullName(name, lastName));
                    } catch (NullPointerException e) {
                        writer.write("Incorrect input\n" + validGetRequests);
                    }
                }
            }
        } else {
            writer.write("Unknown operation\n" + validGetRequests);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validPostRequests = "Valid requests:\n" +
                "/add/?name=SomeName&lastname=SomeLastName\n";
        PrintWriter writer = resp.getWriter();
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        System.out.println("POST");
        if (queryString == null) {
            writer.write("Request needed");
        } else if (pathInfo.equals("add")) {
            Map<String, String[]> parameterMap = req.getParameterMap();
            if (parameterMap != null && !parameterMap.isEmpty()) {
                try {
                    String name = parameterMap.get("name")[0];
                    String lastName = parameterMap.get("lastname")[0];
                    boolean add = personService.addNewEntity(name, lastName);
                    if (add) {
                        writer.write("Entity added");
                    } else {
                        writer.write("Unable to add new entity");
                    }
                } catch (NullPointerException e) {
                    writer.write("Incorrect input\n" + validPostRequests);
                }
            }
        } else {
            writer.write("Invalid request\n" + validPostRequests);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String validPutRequests = "Valid requests:\n" +
                "/edit/?id=x&name=SomeName&lastname=SomeLastName\n";
        PrintWriter writer = resp.getWriter();
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        if (queryString == null) {
            writer.write("request needed");
            writer.write("Current list of entities:\n" + personService.getAllPersons());
        } else if (pathInfo.equals("edit")) {
            Map<String, String[]> parameterMap = req.getParameterMap();

            long id;
            if (parameterMap != null && !parameterMap.isEmpty()) {
                try {
                    String byId = parameterMap.get("id")[0];
                    String name = parameterMap.get("name")[0];
                    String lastName = parameterMap.get("lastname")[0];
                    id = Long.parseLong(byId);
                    boolean update = personService.updateById(id, name, lastName);
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
                "/delete" +
                "/delete/?id=X\n";
        String queryString = req.getQueryString();
        String pathInfo = req.getPathInfo().split("/")[1];
        if (queryString == null && pathInfo.equals("delete")) {
            writer.write("List of banks:\n");
            writer.write("Current list of entities:\n" + personService.getAllPersons());
        } else if (pathInfo.equals("delete")) {
            String byId = req.getParameter("id");
            long id;
            if (byId != null) {
                try {
                    id = Long.parseLong(byId);
                    boolean isDeleted = personService.deleteById(id);
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
