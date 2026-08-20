package com.Deepank.api.specifications;


import com.Deepank.api.config.ConfigManager;
import com.Deepank.api.filters.ApiLoggingFilter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.*;
import io.restassured.specification.*;

public final class RequestSpecFactory {

    private static final String BOOKER_BASEURL = ConfigManager.get("base.url");
    private RequestSpecFactory() {

    }

    public static RequestSpecification getDefaultSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BOOKER_BASEURL)
                .setContentType(ContentType.JSON)
                .setAccept("application/json")
                .addFilter(ApiLoggingFilter.requestLogger())
                .addFilter(ApiLoggingFilter.responseLogger())
                .build();
    }

    public static RequestSpecification getAuthenticatedSpec(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(BOOKER_BASEURL)
                .setContentType(ContentType.JSON)
                .setAccept("application/json")
                .addCookie("token",token)
                .addFilter(ApiLoggingFilter.requestLogger())
                .addFilter(ApiLoggingFilter.responseLogger())
                .build();
    }
}
