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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CreateOrderParameterizedTest extends BaseApi {

    OrderSteps orderSteps = new OrderSteps();
    UserSteps userSteps = new UserSteps();

    private String accessToken;
    private String email;
    private String password;

    private final List<String> ingredients;
    private final int statusCode;

    public CreateOrderParameterizedTest(List<String> ingredients, int statusCode) {
        this.ingredients = ingredients;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] params() {
        return new Object[][]{
                {List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"), SC_OK},
                {List.of(), SC_BAD_REQUEST},
                {List.of("invalidHash1", "invalidHash2"), SC_INTERNAL_SERVER_ERROR},
        };
    }

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
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Response loginResponse = userSteps.loginUser(email, password).extract().response();

        if (loginResponse.getStatusCode() == SC_OK) {
            userSteps.deleteUser(accessToken);
        }
    }

    @Test
    public void createOrderWithAuthTest() {
        Ingredients ingredientsList = new Ingredients(ingredients);
        orderSteps
                .createOrder(accessToken, ingredientsList)
                .statusCode(statusCode);
    }
}
