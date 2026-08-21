package com.Deepank.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "qa");
        String fileName = "config-" + env + ".properties";

        try(InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(fileName)){

            if (inputStream==null){
                throw new RuntimeException("File not found: " + fileName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + fileName, e);
        }
    }

    private ConfigManager() {

    }

    public static String get(String key){

        String envKey = key.toUpperCase().replace(".", "_");

        String envValue = System.getenv(envKey);

        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        String propertyValue = properties.getProperty(key);

        if (propertyValue == null) {
            throw new RuntimeException("Missing configuration key: " + key);
        }
        return propertyValue;
    }

}
