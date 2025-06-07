import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class UpdateUserDataTest {

    UserSteps userSteps = new UserSteps();

    Random random = new Random();
    private String email;
    private String password;
    private String name;

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
    public void changeEmailFieldWithAuthTest() {
        Response loginResponse = userSteps.loginUser(email, password).extract().response();
        String accessToken = loginResponse.path("accessToken");

        email = "something" + random.nextInt(10000000) + "@yandex.ru";

        userSteps
                .updateUserData(accessToken, email, null)
                .statusCode(SC_OK);
    }

    @Test
    public void changeNameFieldWithAuthTest() {
        Response loginResponse = userSteps.loginUser(email, password).extract().response();
        String accessToken = loginResponse.path("accessToken");

        name = RandomStringUtils.randomAlphabetic(6);

        userSteps
                .updateUserData(accessToken, null, name)
                .statusCode(SC_OK);
    }

    @Test
    public void changeEmailFieldWithoutAuth() {
        email = "something" + random.nextInt(10000000) + "@yandex.ru";

        userSteps
                .updateUserData("", email, null)
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void changeNameFieldWithoutAuth() {
        name = RandomStringUtils.randomAlphabetic(6);

        userSteps
                .updateUserData("", null, name)
                .statusCode(SC_UNAUTHORIZED);
    }
}
