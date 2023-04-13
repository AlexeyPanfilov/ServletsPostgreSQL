//import org.junit.ClassRule;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.sql.*;
//import java.util.stream.Collectors;
//
//@Testcontainers
//public class DbIntegrationTest {
//
//    @Container
//    @ClassRule
//    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
//            .withUsername("postgres")
//            .withPassword("admin");
//
//    private static Connection connection;
//    private static Statement statement;
//
//    @BeforeAll
//    public static void connect() {
//        System.out.println("BeforeAll called");
////        postgreSQLContainer.start();
////        try {
////            Class.forName("org.postgresql.Driver");
////            connection = DriverManager.getConnection(
////                    postgreSQLContainer.getJdbcUrl(),
////                    "postgres",
////                    "admin"
////            );
////            statement = connection.createStatement();
////        } catch (SQLException e) {
////            System.out.println("Unable to connect database");
////            e.printStackTrace();
////        } catch (ClassNotFoundException e) {
////            e.printStackTrace();
////        }
//    }
//
//    @BeforeEach
//    public void prepareData() {
//        System.out.println("BeforeEach called");
////        try {
////            String sql = Files.lines(Paths.get("create-tables.sql")).collect(Collectors.joining(" "));
////            statement.execute(sql);
////        } catch (IOException | SQLException e) {
////            e.printStackTrace();
////        }
//    }
//
//    @Test
//    public void test() throws SQLException {
//        System.out.println("Test1 runned");
////        StringBuilder sb = new StringBuilder();
////        ResultSet rs = statement.executeQuery("SELECT * FROM banks WHERE id=1;");
////        while (rs.next()) {
////            sb.append(rs.getLong(1)).append(" ").append(rs.getString("title")).append("\n");
////        }
////        System.out.println(sb);
////        String[] query = {"banks", "Unicredit"};
////        Assertions.assertTrue(queryDispatcher.dispatchAdd(query));
//    }
//
//    @Test
//    public void rsTest() {
//        System.out.println("Test2 runned");
//    }
//
//    @BeforeEach
//    public static void met() {
//        System.out.println("before 2");
//    }
//}
