package com.example.ExchangePoint.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;

class ExchangePointControllerIntegrationTest {

    @Test
    void shouldCreateSession() throws Exception {
        RestAssured.when().post("http://localhost:8080/exchange_point/login{sessionId}", 1L)
                .then().assertThat().statusCode(200);
    }

    @Test
    void shouldCreateRequest() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("type", "BUY");
        requestBody.put("firstCurrency", "UAH");
        requestBody.put("secondCurrency", "USD");
        requestBody.put("currencyAmount", "499");
        requestBody.put("clientPhoneNumber", "9379995");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.post("http://localhost:8080/exchange_point/create_request");

        int statusCode = response.getStatusCode();

        Assertions.assertEquals(200, statusCode);
    }

    @Test
    void shouldReturnResponseWithConfirmCode() {
        RestAssured.get("http://localhost:8080/exchange_point/response{clientPhoneNumber}", 9379995)
                .then().assertThat().statusCode(200)
                .and().body("clientPhoneNumber", is(9379995));
    }

    @Test
    void shouldAskConfirmationCode() throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("confirmationCode", "9379995");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(requestBody.toString());

        Response response = request.put("http://localhost:8080/exchange_point/confirmation{clientPhoneNumber}", 9379995);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void shouldDelete() {
        RestAssured.delete("http://localhost:8080/exchange_point/delete_request{clientPhoneNumber}", 9379995)
                .then().assertThat().statusCode(200);
    }

    @Test
    void shouldReturnReport() {
        RequestSpecification request = RestAssured.given();

        Response response = request.get("http://localhost:8080/exchange_point/report{sessionId}", 1);

        Assertions.assertNotNull(response.getBody());
    }
}