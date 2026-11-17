package com.Deepank.api.models.apiClient.response;

import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private Response response;

    public <T> T as(Class<T> responseClass) {
        return response.as(responseClass);
    }

    public String asString() {
        return response.asString();
    }

    public int getStatusCode() {
        return response.statusCode();
    }

    public String getHeader(String name) {
        return response.getHeader(name);
    }

    public long getResponseTime() {
        return response.time();
    }
}
