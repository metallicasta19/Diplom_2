import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.models.User;
import org.example.steps.OrderSteps;
import org.example.steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class GetOrderListTest extends BaseApi {
    OrderSteps orderSteps = new OrderSteps();
    UserSteps userSteps = new UserSteps();

    private String accessToken;
    private String email;
    private String password;

    @Before
    public void setUp() {
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        password = RandomStringUtils.randomAlphabetic(6);
        String name = faker.name().firstName();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        Response createResponse = userSteps.createUser(user).extract().response();
        accessToken = createResponse.path("accessToken");
    }

    @After
    public void tearDown() {
        Response loginResponse = userSteps.loginUser(email, password).extract().response();

        if (loginResponse.getStatusCode() == SC_OK) {
            userSteps.deleteUser(accessToken);
        }
    }

    @Test
    public void getOrderListWithAuthTest() {
        orderSteps
                .getOrderList(accessToken)
                .statusCode(SC_OK);
    }

    @Test
    public void getOrderListWithoutAuthTest() {
        orderSteps
                .getOrderList("")
                .statusCode(SC_UNAUTHORIZED);
    }
}
