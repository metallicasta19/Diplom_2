package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.models.User;

import static io.restassured.RestAssured.given;
import static org.example.utils.Endpoints.*;

public class UserSteps {
    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_ENDPOINT)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN_USER_ENDPOINT)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .delete(UPDATE_OR_DELETE_USER_ENDPOINT)
                .then();
    }

    @Step("Обновление информации пользователя")
    public ValidatableResponse updateUserData(String accessToken, String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);

        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(UPDATE_OR_DELETE_USER_ENDPOINT)
                .then();
    }
}
