package com.Deepank.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();
    private static final Logger LOG = LoggerFactory.getLogger(ConfigManager.class);

    static {
        String env = System.getProperty("env", "qa");
        LOG.info("env: {}", env);
        String fileName = "config-" + env + ".properties";
        LOG.info("fileName: {}", fileName);

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
        LOG.info("env key:{}", envKey);

        String envValue = System.getenv(envKey);
        LOG.info("env value: {}", envValue);

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
