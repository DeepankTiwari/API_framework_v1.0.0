package com.Deepank.api.models.apiClient.request;

import com.Deepank.api.enums.httpMethods.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiRequest {

    private String endpoint;
    private HttpMethod httpMethod;
    private Map<String, ?> pathParams;
    private Map<String, ?> queryParams;
    private Map<String, ?> headers;
    private Map<String, ?> cookies;
    private Object body;

}
