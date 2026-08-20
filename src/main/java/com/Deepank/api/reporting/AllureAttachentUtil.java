package com.Deepank.api.reporting;

import io.qameta.allure.Allure;

public final class AllureAttachentUtil {

    private AllureAttachentUtil() {

    }

    public static void attachRequest(String request) {
        Allure.addAttachment("API Request", "application/json", request);
    }

    public static void attachResponse(String response) {
        Allure.addAttachment("API Response", "application/json", response);
    }
}
