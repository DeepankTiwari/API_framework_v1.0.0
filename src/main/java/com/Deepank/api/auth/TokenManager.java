package com.Deepank.api.auth;

import com.Deepank.api.config.ConfigManager;
import com.Deepank.api.controllers.AuthControllers;
import com.Deepank.api.models.authSvc.request.AuthRequest;
import com.Deepank.api.models.authSvc.response.AuthResponse;
import lombok.*;

@RequiredArgsConstructor
public class TokenManager {

    private  final String USERNAME = ConfigManager.get("auth.username");
    private  final String PASSWORD = ConfigManager.get("auth.password");
    private final AuthControllers authControllers;
    private String token;

    public synchronized String getToken() {
        if(token == null || token.isBlank()){
            token = generateToken();
        }
        return token;
    }

    private String generateToken() {
        AuthRequest request = AuthRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        AuthResponse response = authControllers.createToken(request, 200);
        if(response.getToken() == null || response.getToken().isBlank()){
            throw new IllegalStateException("Authentication succeeded but token is empty");
        }
        return response.getToken();
    }

    public synchronized void clearToken(){
        token = null;
    }

}
