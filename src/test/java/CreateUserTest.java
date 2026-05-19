import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.models.User;
import org.example.steps.UserSteps;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest extends BaseApi {

    private UserSteps userSteps = new UserSteps();
    private String email;
    private String password;
    private String name;

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
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        password = RandomStringUtils.randomAlphabetic(6);
        name = faker.name().firstName();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        userSteps
                .createUser(user)
                .statusCode(SC_OK);
    }

    @Test
    public void createTwoIdenticalUsersTest() {
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        password = RandomStringUtils.randomAlphabetic(6);
        name = faker.name().firstName();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        userSteps
                .createUser(user)
                .statusCode(SC_OK);

        userSteps
                .createUser(user)
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void createUserWithoutEmailFieldTest() {
        Faker faker = new Faker();
        password = RandomStringUtils.randomAlphabetic(6);
        name = faker.name().firstName();

        User user = new User();
        user.setPassword(password);
        user.setName(name);

        userSteps
                .createUser(user)
                .statusCode(SC_FORBIDDEN)
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutPasswordFieldTest() {
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        name = faker.name().firstName();

        User user = new User();
        user.setEmail(email);
        user.setName(name);

        userSteps
                .createUser(user)
                .statusCode(SC_FORBIDDEN)
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    public void createUserWithoutNameFieldTest() {
        Faker faker = new Faker();
        email = faker.internet().emailAddress();
        password = RandomStringUtils.randomAlphabetic(6);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        userSteps
                .createUser(user)
                .statusCode(SC_FORBIDDEN)
                .body("message",equalTo("Email, password and name are required fields"));
    }
}
