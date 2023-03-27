import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ap.service.QueryDispatcher;

import javax.sql.DataSource;
import java.sql.ResultSet;

@Testcontainers
public class IntegrationTestClass {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withUsername("postgres")
            .withPassword("admin");

    @Test
    public void test() {
        QueryDispatcher queryDispatcher = new QueryDispatcher();
        System.out.println(postgreSQLContainer.getJdbcUrl());
        String[] query = {"banks", "Unicredit"};
        Assertions.assertTrue(queryDispatcher.dispatchAdd(query));
    }

    @Test
    public void rsTest() {

    }
}
