package com.Deepank.api.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AllureEnvironmentUtil {

    public static void writeEnvironmentUtil() {

        Properties properties = new Properties();

        properties.setProperty("Environment", "QA");
        properties.setProperty("base.url", "https://restful-booker.herokuapp.com");
        properties.setProperty("Java version", System.getProperty("java.version"));
        properties.setProperty("OS", System.getProperty("os.name"));

        File resultsDir = new File("build/allure-results");
        resultsDir.mkdirs();

        File environmentFile = new File(resultsDir, "environment.properties");

        try(FileOutputStream outputStream = new FileOutputStream(environmentFile)) {
            properties.store(outputStream, "Allure Environment");
        }catch (IOException e) {
            throw new RuntimeException("Failed to write Allure environment info", e);
        }
    }
}
