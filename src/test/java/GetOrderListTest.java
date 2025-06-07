import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Steps.OrderSteps;
import org.example.Steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class GetOrderListTest {
    OrderSteps orderSteps = new OrderSteps();
    UserSteps userSteps = new UserSteps();

    private String accessToken;
    private String email;
    private String password;

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
