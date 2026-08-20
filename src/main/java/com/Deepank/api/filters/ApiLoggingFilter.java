package com.Deepank.api.filters;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public final class ApiLoggingFilter {

    private ApiLoggingFilter() {

    }

    public static RequestLoggingFilter requestLogger() {
        return new RequestLoggingFilter();
    }

    public static ResponseLoggingFilter responseLogger() {
        return new ResponseLoggingFilter();
    }
}
