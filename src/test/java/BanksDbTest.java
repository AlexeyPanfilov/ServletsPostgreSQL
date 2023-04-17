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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    public static void getQueryDispatcher() {
        System.out.println("GetQueryDispatcher called");
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
    void add() {
        String[] query = {"banks", "MyOwnBank"};
        Assertions.assertTrue(queryDispatcher.dispatchAdd(query));
    }

    @Test
    @DisplayName("Get by id test")
    void getById() {
        String table = "banks";
        long id = 1;
        Assertions.assertEquals("1. Sber", queryDispatcher.dispatchGetById(table, id));
    }

    @Test
    @DisplayName("Get by title test (clients and cards lists)")
    void getByTitle() {
        String table = "banks";
        String title = "VTB";
        String result = "Clients:\n" +
                "Mika Hakkinen\n" +
                "John Smith\n" +
                "Cards:\n" +
                "2222 5555\n" +
                "2222 1234\n";
        Assertions.assertEquals(result, queryDispatcher.dispatchGetByName(table, title));
    }

    @Test
    @DisplayName("Get list of banks")
    void getListOfBanks() {
        String result = "1 Sber\n" +
                "2 VTB\n" +
                "3 Alfa\n" +
                "4 GPB";
        Assertions.assertEquals(result, queryDispatcher.dispatchGetTables("banks"));
    }

    @Test
    @DisplayName("Get banks with cards list")
    void getClients() {
        String result = "VTB 2222 5555\n" +
                "VTB 2222 1234\n" +
                "GPB 4444 5555\n" +
                "GPB 4444 1212\n" +
                "GPB 4444 5633\n" +
                "Alfa 3333 1234\n" +
                "Alfa 3333 9876\n" +
                "Sber 1111 5678\n" +
                "Sber 1111 1234\n" +
                "Sber 1111 2367\n";
        Assertions.assertEquals(result, queryDispatcher.dispatchGetTables("banks_cards"));
    }

    @Test
    @DisplayName("Add test")
    void addBank() {
        String[] query = {"banks", "UBS"};
        long id = 5;
        Assertions.assertTrue(queryDispatcher.dispatchAdd(query));
        Assertions.assertEquals(id + ". " + query[1], queryDispatcher.dispatchGetById(query[0], id));
    }

    @Test
    @DisplayName("Update test")
    void updateBank() {
        String  id = "1";
        String newTitle = "NewBank";
        String[] query = {"banks", id, newTitle};
        Assertions.assertTrue(queryDispatcher.dispatchUpdateById(query));
        Assertions.assertEquals(
                Long.parseLong(id) + ". " + query[2], queryDispatcher.dispatchGetById(query[0], Long.parseLong(id))
        );
    }

    @Test
    @DisplayName("Delete test")
    void deleteBank() {
        long id = 3;
        String table = "banks";
        String titleOfDeletingBank = queryDispatcher.dispatchGetById(table, id);
        Assertions.assertTrue(queryDispatcher.dispatchDeleteById(table, id));
        Assertions.assertNotEquals(
                id + ". " + titleOfDeletingBank, queryDispatcher.dispatchGetById(table, id)
        );
    }
}
