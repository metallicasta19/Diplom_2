import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.apache.http.HttpStatus.*;

public class LoginUserTest {

    private String email;
    private String password;
    private String name;

    UserSteps userSteps = new UserSteps();
    Random random = new Random();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        email = "something" + random.nextInt(10000000) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(6);
        name = RandomStringUtils.randomAlphabetic(6);

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
    public void loginWithExistingUserTest() {
        userSteps
                .loginUser(email, password)
                .statusCode(SC_OK);
    }

    @Test
    public void loginWithInvalidLoginTest() {
        email = "something" + random.nextInt(10000000) + "@yandex.ru";

        userSteps
                .loginUser(email, password)
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void loginWithInvalidPasswordTest() {
        password = RandomStringUtils.randomAlphabetic(6);

        userSteps
                .loginUser(email, password)
                .statusCode(SC_UNAUTHORIZED);
    }
}
