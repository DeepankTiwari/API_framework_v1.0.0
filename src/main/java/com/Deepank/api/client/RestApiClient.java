package com.Deepank.api.client;

import com.Deepank.api.models.apiClient.request.ApiRequest;
import com.Deepank.api.specifications.RequestSpecFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class RestApiClient {

    protected Response processRequest(ApiRequest apiRequest) {

        RequestSpecification request = buildRequest(apiRequest);

         return executeRequest(request, apiRequest);
    }

    private RequestSpecification buildRequest(ApiRequest apiRequest) {

        RequestSpecification request = given()
                .spec(RequestSpecFactory.getDefaultSpec());

        if (apiRequest.getPathParams() != null && !apiRequest.getPathParams().isEmpty()) {
            request.pathParams(apiRequest.getPathParams());
        }
        if (apiRequest.getQueryParams() != null && !apiRequest.getQueryParams().isEmpty()) {
            request.queryParams(apiRequest.getQueryParams());
        }
        if (apiRequest.getHeaders() != null && !apiRequest.getHeaders().isEmpty()) {
            request.headers(apiRequest.getHeaders());
        }
        if (apiRequest.getCookies() != null && !apiRequest.getCookies().isEmpty()) {
            request.cookies(apiRequest.getCookies());
        }
        if (apiRequest.getBody() != null) {
            request.body(apiRequest.getBody());
        }
        return request;
    }

    private Response executeRequest(RequestSpecification request, ApiRequest apiRequest) {

        return switch (apiRequest.getHttpMethod()){
            case GET -> request.get(apiRequest.getEndpoint());
            case PUT -> request.put(apiRequest.getEndpoint());
            case POST -> request.post(apiRequest.getEndpoint());
            case PATCH -> request.patch(apiRequest.getEndpoint());
            case DELETE -> request.delete(apiRequest.getEndpoint());
            default -> throw new RuntimeException("Invalid HTTP method -> " + apiRequest.getHttpMethod());
        };
    }
}
