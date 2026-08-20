package com.Deepank.api.services;

import com.Deepank.api.client.ApiClient;
import com.Deepank.api.models.request.AuthRequest;
import com.Deepank.api.models.response.AuthResponse;
import io.restassured.response.Response;


public class AuthService {

    private static final String AUTH_ENDPOINT = "/auth";

    private final ApiClient apiClient = new ApiClient();

    public AuthResponse getAuthToken(AuthRequest request) {

        Response response = apiClient.post(AUTH_ENDPOINT, request);
        return response.as(AuthResponse.class);
    }
}
