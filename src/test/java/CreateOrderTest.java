import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Steps.OrderSteps;
import org.example.Steps.UserSteps;
import org.example.models.Ingredients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class CreateOrderTest {
    OrderSteps orderSteps = new OrderSteps();
    UserSteps userSteps = new UserSteps();

    private String email;
    private String password;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        Random random = new Random();
        email = "something" + random.nextInt(10000000) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(6);
        String name = RandomStringUtils.randomAlphabetic(6);

        userSteps.createUser(email, password, name);
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
