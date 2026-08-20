package com.Deepank.api.auth;

import com.Deepank.api.config.ConfigManager;
import com.Deepank.api.models.request.AuthRequest;
import com.Deepank.api.models.response.AuthResponse;
import com.Deepank.api.services.AuthService;

public class TokenManager {

    private static final String USERNAME = ConfigManager.get("auth.username");
    private static final String PASSWORD = ConfigManager.get("auth.password");
    private final AuthService authService = new AuthService();

    public String getToken() {

        AuthRequest request = AuthRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        AuthResponse response = authService.getAuthToken(request);
        return response.getToken();
    }
}
