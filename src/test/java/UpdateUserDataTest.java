import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.models.User;
import org.example.steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class UpdateUserDataTest extends BaseApi {

    UserSteps userSteps = new UserSteps();

    Faker faker = new Faker();
    private String email;
    private String password;
    private String name;

    @Before
    public void setUp() {
        email = faker.internet().emailAddress();
        password = RandomStringUtils.randomAlphabetic(6);
        name = faker.name().firstName();

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
    public void changeEmailFieldWithAuthTest() {
        Response loginResponse = userSteps.loginUser(email, password).extract().response();
        String accessToken = loginResponse.path("accessToken");

        email = faker.internet().emailAddress();

        userSteps
                .updateUserData(accessToken, email, null)
                .statusCode(SC_OK);
    }

    @Test
    public void changeNameFieldWithAuthTest() {
        Response loginResponse = userSteps.loginUser(email, password).extract().response();
        String accessToken = loginResponse.path("accessToken");

        name = faker.name().firstName();

        userSteps
                .updateUserData(accessToken, null, name)
                .statusCode(SC_OK);
    }

    @Test
    public void changeEmailFieldWithoutAuth() {
        email = faker.internet().emailAddress();

        userSteps
                .updateUserData("", email, null)
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void changeNameFieldWithoutAuth() {
        name = faker.name().firstName();

        userSteps
                .updateUserData("", null, name)
                .statusCode(SC_UNAUTHORIZED);
    }
}
