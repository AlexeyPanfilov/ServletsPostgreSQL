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
public class CardsDbTest {

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
//    @Test
//    @DisplayName("Add test")
//    void add() {
//        String[] query = {"cards", "1111 4522", "1", "2"};
//        Assertions.assertTrue(queryDispatcher.dispatchAdd(query));
//        Assertions.assertEquals(query[1] + ", " + query[2] + ". Sber, " + query[3] + ". Vladimir Ivanov",
//                queryDispatcher.dispatchGetById(query[0], 11));
//    }
//
//    @Test
//    @DisplayName("Get by id test")
//    void getById() {
//        String table = "cards";
//        long id = 1;
//        String number = "";
//        StringBuilder sb = new StringBuilder();
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    "SELECT c.number, b.id, b.title, p.id, p.name, p.lastname " +
//                            "FROM cards c " +
//                            "INNER JOIN banks b ON c.bank_id = b.id " +
//                            "INNER JOIN persons p ON c.person_id = p.id " +
//                            "WHERE c.id=?;"
//            );
//            preparedStatement.setLong(1, id);
//            try (ResultSet rs = preparedStatement.executeQuery()) {
//                while (rs.next()) {
//                    sb.append(rs.getString(1))
//                            .append(", ")
//                            .append(rs.getString(2))
//                            .append(". ")
//                            .append(rs.getString(3))
//                            .append(", ")
//                            .append(rs.getString(4))
//                            .append(". ")
//                            .append(rs.getString(5))
//                            .append(" ")
//                            .append(rs.getString(6));
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String result = sb.toString();
//        Assertions.assertEquals(result, queryDispatcher.dispatchGetById(table, id));
//    }
//
//    @Test
//    @DisplayName("Get list of cards")
//    void getListOfCards() {
//        StringBuilder sb = new StringBuilder();
//        try (ResultSet rs = statement.executeQuery(
//                "SELECT c.id, c.number, b.title, p.name, p.lastname " +
//                "FROM cards c " +
//                "INNER JOIN banks b ON c.bank_id = b.id " +
//                "INNER JOIN persons p ON c.person_id = p.id;"
//        )) {
//            while (rs.next()) {
//                sb.append(rs.getString(1))
//                        .append(". ")
//                        .append(rs.getString(2))
//                        .append(", ")
//                        .append(rs.getString(3))
//                        .append(", ")
//                        .append(rs.getString(4))
//                        .append(" ")
//                        .append(rs.getString(5))
//                        .append("\n");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String result = sb.toString().trim();
//        Assertions.assertEquals(result, queryDispatcher.dispatchGetTables("cards"));
//    }
//
//    @Test
//    @DisplayName("Delete test")
//    void deleteById() {
//        long id = 3;
//        String table = "cards";
//        String numberOfDeletingCard = queryDispatcher.dispatchGetById(table, id);
//        Assertions.assertTrue(queryDispatcher.dispatchDeleteById(table, id));
//        Assertions.assertNotEquals(
//                numberOfDeletingCard, queryDispatcher.dispatchGetById(table, id)
//        );
//    }
}
