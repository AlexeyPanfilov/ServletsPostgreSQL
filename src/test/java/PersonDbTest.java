import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.stream.Collectors;

@Testcontainers
public class PersonDbTest {

    @Container
    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres")
            .withUsername("postgres")
            .withDatabaseName("my_data")
            .withPassword("admin");

    private static Connection connection;
    private static Statement statement;
//    private static QueryDispatcher queryDispatcher;
//
//    @BeforeAll
//    public static void connect() {
//        System.out.println("BeforeAll called");
//        postgreSQLContainer.start();
//        try {
//            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(
//                    postgreSQLContainer.getJdbcUrl(),
//                    postgreSQLContainer.getUsername(),
//                    postgreSQLContainer.getPassword()
//            );
//            statement = connection.createStatement();
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @BeforeAll
//    public static void getQueryDispatcher() {
//        System.out.println("GetQoeryDispatcher called");
//        queryDispatcher = new QueryDispatcher(
//                "org.postgresql.Driver",
//                postgreSQLContainer.getJdbcUrl(),
//                postgreSQLContainer.getUsername(),
//                postgreSQLContainer.getPassword()
//        );
//    }
//
//    @BeforeEach
//    public void prepareData() {
//        System.out.println("BeforeEach called");
//        try {
//            String sql = Files.lines(Paths.get("create-tables.sql")).collect(Collectors.joining(" "));
//            statement.execute(sql);
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @AfterAll
//    public static void disconnect() {
//        try {
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            connection.close();
//            System.out.println("Disconnected");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    @DisplayName("Add test")
//    void add() {
//        String[] query = {"persons", "Person", "One"};
//        long id = -1;
//        Assertions.assertTrue(queryDispatcher.dispatchAdd(query));
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    "SELECT id FROM persons WHERE (name, lastname)=(?,?);"
//            );
//            preparedStatement.setString(1, query[1]);
//            preparedStatement.setString(2, query[2]);
//            try (ResultSet rs = preparedStatement.executeQuery()) {
//                while (rs.next()) {
//                    id = rs.getLong(1);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        Assertions.assertEquals(id + ". " + query[1] + " " + query[2], queryDispatcher.dispatchGetById(query[0], id));
//    }
//
//    @Test
//    @DisplayName("Get by id test")
//    void getById() {
//        String table = "persons";
//        long id = 1;
//        String name = "";
//        String lastName = "";
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    "SELECT * FROM persons WHERE id=?;"
//            );
//            preparedStatement.setLong(1, id);
//            try (ResultSet rs = preparedStatement.executeQuery()) {
//                while (rs.next()) {
//                    name = rs.getString(2);
//                    lastName = rs.getString(3);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        Assertions.assertEquals(id + ". " + name + " " + lastName, queryDispatcher.dispatchGetById(table, id));
//    }
//
//    @Test
//    @DisplayName("Get by full name test (banks and cards lists)")
//    void getByTitle() {
//        String table = "persons";
//        String name = "John";
//        String lastName = "Smith";
//        String result = "Banks:\n" +
//                "Sber\n" +
//                "VTB\n" +
//                "GPB\n" +
//                "GPB\n" +
//                "Cards:\n" +
//                "1111 5678\n" +
//                "2222 1234\n" +
//                "4444 5555\n" +
//                "4444 1212\n";
//        Assertions.assertEquals(result, queryDispatcher.dispatchGetByName(table, name + "_" + lastName));
//    }
//
//    @Test
//    @DisplayName("Get list of persons")
//    void getListOfPersons() {
//        StringBuilder sb = new StringBuilder();
//        try (ResultSet rs = statement.executeQuery("SELECT * FROM persons;")) {
//            while (rs.next()) {
//                sb.append(rs.getLong(1))
//                        .append(" ")
//                        .append(rs.getString(2))
//                        .append(" ")
//                        .append(rs.getString(3))
//                        .append("\n");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String result = sb.toString().trim();
//        Assertions.assertEquals(result, queryDispatcher.dispatchGetTables("persons"));
//    }
//
//    @Test
//    @DisplayName("Get persons with cards list")
//    void getCards() {
//        StringBuilder sb = new StringBuilder();
//        try (ResultSet rs = statement.executeQuery(
//                "SELECT p.name, p.lastname, c.number FROM cards c INNER JOIN persons p ON c.person_id = p.id;"
//        )) {
//            while (rs.next()) {
//                sb.append(rs.getString(1))
//                        .append(" ")
//                        .append(rs.getString(2))
//                        .append(" ")
//                        .append(rs.getString(3))
//                        .append("\n");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String result = sb.toString().trim();
//        Assertions.assertEquals(result, queryDispatcher.dispatchGetTables("persons_cards"));
//    }
//
//    @Test
//    @DisplayName("Update test")
//    void updateById() {
//        String id = "1";
//        String newName = "Rocco";
//        String newLastName = "Siffredi";
//        String[] query = {"persons", id, newName, newLastName};
//        Assertions.assertTrue(queryDispatcher.dispatchUpdateById(query));
//        Assertions.assertEquals(
//                Long.parseLong(id) + ". " + query[2] + " " + query[3],
//                queryDispatcher.dispatchGetById(query[0], Long.parseLong(id))
//        );
//    }
//
//    @Test
//    @DisplayName("Delete test")
//    void deleteById() {
//        long id = 3;
//        String table = "persons";
//        String fullNameOfDeletingPerson = queryDispatcher.dispatchGetById(table, id);
//        Assertions.assertTrue(queryDispatcher.dispatchDeleteById(table, id));
//        Assertions.assertNotEquals(
//                fullNameOfDeletingPerson, queryDispatcher.dispatchGetById(table, id)
//        );
//    }
}
