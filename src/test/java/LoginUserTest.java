import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.models.User;
import org.example.steps.UserSteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class LoginUserTest extends BaseApi {

    private String email;
    private String password;
    private String name;

    UserSteps userSteps = new UserSteps();
    Faker faker = new Faker();

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
    public void loginWithExistingUserTest() {
        userSteps
                .loginUser(email, password)
                .statusCode(SC_OK);
    }

    @Test
    public void loginWithInvalidLoginTest() {
        String invalidEmail = faker.internet().emailAddress();

        userSteps
                .loginUser(invalidEmail, password)
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    public void loginWithInvalidPasswordTest() {
        String invalidPassword = RandomStringUtils.randomAlphabetic(6);

        userSteps
                .loginUser(email, invalidPassword)
                .statusCode(SC_UNAUTHORIZED);
    }
}
