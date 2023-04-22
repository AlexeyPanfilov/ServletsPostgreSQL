import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ap.db.DataBase;
import ru.ap.entities.Card;
import ru.ap.repository.CardRepository;

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
    private static CardRepository cardRepository;

    @BeforeAll
    public static void connect() {
        postgreSQLContainer.start();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    postgreSQLContainer.getJdbcUrl(),
                    postgreSQLContainer.getUsername(),
                    postgreSQLContainer.getPassword()
            );
            statement = connection.createStatement();
             cardRepository = new CardRepository(new DataBase(
                    "org.postgresql.Driver",
                    postgreSQLContainer.getJdbcUrl(),
                    postgreSQLContainer.getUsername(),
                    postgreSQLContainer.getPassword()
            ));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void prepareData() {
        try {
            String sql = Files.lines(Paths.get("create-and-fill-test-tables.sql")).collect(Collectors.joining(" "));
            statement.execute(sql);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Add test")
    void add() {
        Card card = new Card("1111 4522", 1, 2);
        Assertions.assertTrue(cardRepository.addCard(card));
        Assertions.assertEquals("11. " + "1111 4522",
                cardRepository.getCardInfoById(11).toString());
    }

    @Test
    @DisplayName("Get by id test")
    void getById() {
        long id = 1;
        StringBuilder sb = new StringBuilder();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT c.number, b.id, p.id " +
                            "FROM cards c " +
                            "INNER JOIN banks b ON c.bank_id = b.id " +
                            "INNER JOIN persons p ON c.person_id = p.id " +
                            "WHERE c.id=?;"
            );
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    sb.append(rs.getString(1))
                            .append(", ")
                            .append(rs.getString(2))
                            .append(" ")
                            .append(rs.getString(3));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String result = sb.toString();
        Card card = cardRepository.getCardInfoById(id);
        Assertions.assertEquals(result, card.getCardNumber() + ", " + card.getOwnerId() + " " + card.getBankId());
    }

    @Test
    @DisplayName("Delete test")
    void deleteById() {
        long id = 3;
        String numberOfDeletingCard = cardRepository.getCardInfoById(id).getCardNumber();
        Assertions.assertTrue(cardRepository.deleteById(id));
        Assertions.assertNotEquals(numberOfDeletingCard, cardRepository.getCardInfoById(id));
    }
}
