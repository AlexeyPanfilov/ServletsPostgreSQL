import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ap.db.DataBase;
import ru.ap.entities.Bank;
import ru.ap.entities.Card;
import ru.ap.entities.Person;
import ru.ap.repository.BankRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Testcontainers
public class BanksDbTest {
//
//    @Container
//    @ClassRule
//    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres")
//            .withUsername("postgres")
//            .withDatabaseName("my_data")
//            .withPassword("admin");
//
//    private static Connection connection;
//    private static Statement statement;
//    private static BankRepository bankRepository;
//
//    @BeforeAll
//    public static void connect() {
//        postgreSQLContainer.start();
//        try {
//            Class.forName("org.postgresql.Driver");
//            connection = DriverManager.getConnection(
//                    postgreSQLContainer.getJdbcUrl(),
//                    postgreSQLContainer.getUsername(),
//                    postgreSQLContainer.getPassword()
//            );
//            statement = connection.createStatement();
//            bankRepository = new BankRepository(new DataBase(
//                    "org.postgresql.Driver",
//                    postgreSQLContainer.getJdbcUrl(),
//                    postgreSQLContainer.getUsername(),
//                    postgreSQLContainer.getPassword()
//            ));
//
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @BeforeEach
//    public void prepareData() {
//        try {
//            String sql = Files.lines(Paths.get("create-and-fill-test-tables.sql")).collect(Collectors.joining(" "));
//            statement.execute(sql);
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @DisplayName("Add test")
//    void add() {
//        Bank bank = new Bank("MyOwnBank");
//        Assertions.assertTrue(bankRepository.addNewEntity(bank));
//    }
//
//    @Test
//    @DisplayName("Get by id test")
//    void getById() {
//        long id = 1;
//        Assertions.assertEquals("1. Sber", bankRepository.getById(id).toString());
//    }
//
//    @Test
//    @DisplayName("Get by title test")
//    void getByTitle() {
//        String title = "VTB";
//        String result = "2. VTB";
//        Assertions.assertEquals(result, bankRepository.getByTitle(title).toString());
//    }
//
//    @Test
//    @DisplayName("Get list of banks")
//    void getListOfBanks() {
//        String result = "1 Sber\n" +
//                "2 VTB\n" +
//                "3 Alfa\n" +
//                "4 GPB";
//        Assertions.assertEquals(result, bankRepository.getBanksList());
//    }
//
//    @Test
//    @DisplayName("Show clients of bank")
//    void getClients() {
//        long id = 2;
//        List<Person> clients = new ArrayList<>();
//        Person p1 = new Person("John", "Smith");
//        p1.setId(1);
//        Person p2 = new Person("Mika", "Hakkinen");
//        p2.setId(4);
//        clients.add(p1);
//        clients.add(p2);
//        Assertions.assertEquals(clients.toString(), bankRepository.getClientsById(id).toString());
//    }
//
//    @Test
//    @DisplayName("Show list of cards")
//    void getCards() {
//        long id = 2;
//        List<Card> clients = new ArrayList<>();
//        Card c1 = new Card("2222 5555", 2, 4);
//        c1.setId(4);
//        Card c2 = new Card("2222 1234", 2, 1);
//        c2.setId(5);
//        clients.add(c1);
//        clients.add(c2);
//        Assertions.assertEquals(clients.toString(), bankRepository.getCardsById(id).toString());
//    }
//
//    @Test
//    @DisplayName("Add test")
//    void addBank() {
//        long id = 5;
//        Bank bank = new Bank("UBS");
//        Assertions.assertTrue(bankRepository.addNewEntity(bank));
//        Assertions.assertEquals(id + ". " + "UBS", bankRepository.getById(id).toString());
//    }
//
//    @Test
//    @DisplayName("Update test")
//    void updateById() {
//        long id = 1;
//        String newTitle = "NewBank";
//        Assertions.assertTrue(bankRepository.updateById(id, newTitle));
//        Assertions.assertEquals(id + ". " + newTitle, bankRepository.getById(id).toString());
//    }
//
//    @Test
//    @DisplayName("Delete test")
//    void deleteById() {
//        long id = 3;
//        String titleOfDeletingBank = bankRepository.getById(id).toString();
//        Assertions.assertTrue(bankRepository.deleteById(id));
//        Assertions.assertNotEquals(id + ". " + titleOfDeletingBank, bankRepository.getById(id).toString());
//    }
}
