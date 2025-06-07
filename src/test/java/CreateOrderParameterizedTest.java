import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Steps.OrderSteps;
import org.example.Steps.UserSteps;
import org.example.models.Ingredients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;
import java.util.Random;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CreateOrderParameterizedTest {

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
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        Random random = new Random();
        email = "something" + random.nextInt(10000000) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(6);
        String name = RandomStringUtils.randomAlphabetic(6);

        Response createResponse = userSteps.createUser(email, password, name).extract().response();
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
    public void createOrderWithAuthTest() {
        Ingredients ingredientsList = new Ingredients(ingredients);
        orderSteps
                .createOrder(accessToken, ingredientsList)
                .statusCode(statusCode);
    }
}
