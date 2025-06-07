package org.example.Steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.models.Ingredients;

import static io.restassured.RestAssured.given;
import static org.example.utils.Endpoints.CREATE_OR_GET_ORDER_ENDPOINT;

public class OrderSteps {
    @Step("Создание заказа")
    public ValidatableResponse createOrder(String accessToken, Ingredients ingredientsList) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .and()
                .body(ingredientsList)
                .when()
                .post(CREATE_OR_GET_ORDER_ENDPOINT)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", accessToken)
                .when()
                .get(CREATE_OR_GET_ORDER_ENDPOINT)
                .then();
    }
}
