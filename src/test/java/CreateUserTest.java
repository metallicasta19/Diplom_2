import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {

    private UserSteps userSteps = new UserSteps();
    private String email;
    private String password;
    private String name;
    private Random random = new Random();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
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
    public void createUserWithAllRequiredFieldsTest() {
        email = "something" + random.nextInt(10000000) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(6);
        name = RandomStringUtils.randomAlphabetic(6);

        userSteps
                .createUser(email, password, name)
                .statusCode(SC_OK);
    }

    @Test
    public void createTwoIdenticalUsersTest() {
        email = "something" + random.nextInt(10000000) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(6);
        name = RandomStringUtils.randomAlphabetic(6);

        userSteps
                .createUser(email, password, name)
                .statusCode(SC_OK);

        userSteps
                .createUser(email, password, name)
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void createUserWithoutEmailFieldTest() {
        password = RandomStringUtils.randomAlphabetic(6);
        name = RandomStringUtils.randomAlphabetic(6);

        userSteps
                .createUser(email, password, name)
                .statusCode(SC_FORBIDDEN)
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutPasswordFieldTest() {
        email = "something" + random.nextInt(10000000) + "@yandex.ru";
        name = RandomStringUtils.randomAlphabetic(6);

        userSteps
                .createUser(email, password, name)
                .statusCode(SC_FORBIDDEN)
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutNameFieldTest() {
        email = "something" + random.nextInt(10000000) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(6);

        userSteps
                .createUser(email, password, name)
                .statusCode(SC_FORBIDDEN)
                .body("message",equalTo("Email, password and name are required fields"));
    }
}
