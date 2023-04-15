import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ap.service.QueryDispatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.stream.Collectors;

@Testcontainers
public class BanksDbTest {

    @Container
    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres")
            .withUsername("postgres")
            .withDatabaseName("my_data")
            .withPassword("admin");

    private static Connection connection;
    private static Statement statement;
    private static QueryDispatcher queryDispatcher;

    @BeforeAll
    public static void connect() {
        System.out.println("BeforeAll called");
        postgreSQLContainer.start();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    postgreSQLContainer.getJdbcUrl(),
                    postgreSQLContainer.getUsername(),
                    postgreSQLContainer.getPassword()
            );
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Unable to connect database");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    public static void getQueryDispatcher() {
        System.out.println("GetQoeryDispatcher called");
        queryDispatcher = new QueryDispatcher(
                "org.postgresql.Driver",
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        );
    }

    @BeforeEach
    public void prepareData() {
        System.out.println("BeforeEach called");
        try {
            String sql = Files.lines(Paths.get("create-tables.sql")).collect(Collectors.joining(" "));
            statement.execute(sql);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Add test")
    public void add() {
        String[] query = {"banks", "MyOwnBank"};
        Assertions.assertTrue(queryDispatcher.dispatchAdd(query));
    }

    @Test
    @DisplayName("Read test")
    public void getById() {
        String table = "banks";
        long id = 1;
        Assertions.assertEquals("1. Sber", queryDispatcher.dispatchGetById(table, id));
    }
}
