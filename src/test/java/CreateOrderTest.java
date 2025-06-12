import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.models.User;
import org.example.steps.OrderSteps;
import org.example.steps.UserSteps;
import org.example.models.Ingredients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class CreateOrderTest extends BaseApi {
    OrderSteps orderSteps = new OrderSteps();
    UserSteps userSteps = new UserSteps();

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

        userSteps.createUser(user);
    }

    @After
    public void tearDown() {
        Response loginResponse = userSteps.loginUser(email, password).extract().response();

        if (loginResponse.getStatusCode() == SC_OK) {
            String accessToken = loginResponse.path("accessToken");
            userSteps.deleteUser(accessToken);
        }
    }

    @Test
    public void createOrderWithoutAuthTest() {
        Ingredients ingredientsList = new Ingredients(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        orderSteps
                .createOrder("", ingredientsList)
                .statusCode(SC_UNAUTHORIZED);
    }
}
