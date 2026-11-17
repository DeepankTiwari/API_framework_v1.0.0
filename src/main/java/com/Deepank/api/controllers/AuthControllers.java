package com.Deepank.api.controllers;

import com.Deepank.api.client.RestApiClient;
import com.Deepank.api.enums.endpoints.AuthServiceUrl;
import com.Deepank.api.enums.httpMethods.HttpMethod;
import com.Deepank.api.models.apiClient.request.ApiRequest;
import com.Deepank.api.models.authSvc.request.AuthRequest;
import com.Deepank.api.models.authSvc.response.AuthResponse;
import io.restassured.response.Response;
import org.testng.Assert;

public class AuthControllers extends RestApiClient {

    public AuthResponse createToken(AuthRequest requestBody, int expectedStatusCode){

        String endpoint = AuthServiceUrl.AUTH.getPath();
        ApiRequest apiRequest = ApiRequest.builder()
                .endpoint(endpoint)
                .httpMethod(HttpMethod.POST)
                .body(requestBody)
                .build();

        Response response = processRequest(apiRequest);

        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Response StatusCode mismatch");

        return response.as(AuthResponse.class);
    }
}
