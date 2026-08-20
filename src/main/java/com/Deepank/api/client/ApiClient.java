package com.Deepank.api.client;

import com.Deepank.api.specifications.RequestSpecFactory;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiClient {

    public Response get(String endpoint) {
        return given()
                .spec(RequestSpecFactory.getDefaultSpec())
                .when()
                .get(endpoint);
    }

    public Response post(String endpoint, Object body) {
        return given()
                .spec(RequestSpecFactory.getDefaultSpec())
                .body(body)
                .when()
                .post(endpoint);
    }

    public Response put(String endpoint, Object body) {
        return given()
                .spec(RequestSpecFactory.getDefaultSpec())
                .body(body)
                .when()
                .put(endpoint);
    }

    public Response put(String endpoint, Object body, String token) {
        return given()
                .spec(RequestSpecFactory.getAuthenticatedSpec(token))
                .log().all()
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response patch(String endpoint, Object body) {
        return given()
                .spec(RequestSpecFactory.getDefaultSpec())
                .body(body)
                .when()
                .patch(endpoint);
    }

    public Response patch(String endpoint, Object body, String token) {
        return given()
                .spec(RequestSpecFactory.getAuthenticatedSpec(token))
                .body(body)
                .when()
                .patch(endpoint);
    }

    public Response delete(String endpoint) {
        return given()
                .spec(RequestSpecFactory.getDefaultSpec())
                .when()
                .delete(endpoint);
    }

    public Response delete(String endpoint, String token) {
        return given()
                .spec(RequestSpecFactory.getAuthenticatedSpec(token))
                .when()
                .delete(endpoint);
    }
}
