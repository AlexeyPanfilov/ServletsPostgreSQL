import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ap.db.DataBase;
import ru.ap.entities.Person;
import ru.ap.repository.PersonRepository;

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
    private static PersonRepository personRepository;

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
            personRepository = new PersonRepository(new DataBase(
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

    @AfterAll
    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
            System.out.println("Disconnected");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("Add test")
    void add() {
        Person person = new Person("New", "Person");
        Assertions.assertTrue(personRepository.addNewEntity(person));
    }

    @Test
    @DisplayName("Get by id test")
    void getById() {
        long id = 1;
        String name = "";
        String lastName = "";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM persons WHERE id=?;"
            );
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    name = rs.getString(2);
                    lastName = rs.getString(3);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(id + ". " + name + " " + lastName, personRepository.getById(id).toString());
    }

    @Test
    @DisplayName("Get by name and lastName test")
    void getByFullName() {
        StringBuilder sb = new StringBuilder();
        String name = "John";
        String lastName = "Smith";
        long id = -1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM persons WHERE (name, lastname) = (?, ?);"
            );
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    id = rs.getLong(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sb.append(id).append(". ").append(name).append(" ").append(lastName);
        Assertions.assertEquals(sb.toString(), personRepository.getByFullName(name, lastName).toString());
    }

    @Test
    @DisplayName("Get list of persons")
    void getListOfPersons() {
        StringBuilder sb = new StringBuilder();
        try (ResultSet rs = statement.executeQuery("SELECT * FROM persons;")) {
            while (rs.next()) {
                sb.append(rs.getLong(1))
                        .append(" ")
                        .append(rs.getString(2))
                        .append(" ")
                        .append(rs.getString(3))
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String result = sb.toString().trim();
        Assertions.assertEquals(result, personRepository.getPersonsList());
    }

    @Test
    @DisplayName("Get cards list by person id")
    void getCards() {
        long id = 1;
        StringBuilder sb = new StringBuilder("[");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT c.id, c.number FROM cards c INNER JOIN persons p ON c.person_id = p.id WHERE p.id=?;"
            );
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    sb.append(rs.getString(1))
                            .append(". ")
                            .append(rs.getString(2))
                            .append(", ");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String result = sb.deleteCharAt(sb.lastIndexOf(",")).toString().trim();
        Assertions.assertEquals(result + "]", personRepository.getCardsById(id).toString());
    }

    @Test
    @DisplayName("Update test")
    void updateById() {
        String id = "1";
        String newName = "Rocco";
        String newLastName = "Siffredi";
        Assertions.assertTrue(personRepository.updateById(Long.parseLong(id), newName, newLastName));
        Assertions.assertEquals(
                Long.parseLong(id) + ". " + newName + " " + newLastName,
                personRepository.getById(Long.parseLong(id)).toString()
        );
    }

    @Test
    @DisplayName("Delete test")
    void deleteById() {
        long id = 3;
        String fullNameOfDeletingPerson = personRepository.getById(id).toString();
        Assertions.assertTrue(personRepository.deleteById(id));
        Assertions.assertNotEquals(
                fullNameOfDeletingPerson, personRepository.getById(id).toString()
        );
    }
}
