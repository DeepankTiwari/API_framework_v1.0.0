package com.Deepank.api.config;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

@UtilityClass
public class ConfigManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigManager.class);
    private final String DEFAULT_ENV = "qa";
    private final String ENVIRONMENT = resolveEnv();
    private final Properties PROPERTIES = loadProperties();

    public String getEnv(){
        return ENVIRONMENT;
    }

    public String get(String key){

        if(!hasValue(key)){
            throw new IllegalArgumentException("Configuration key must not be blank or null -> " + key);
        }
        String envKey = key.toUpperCase().replace(".", "_");

        // Env variable
        String envValue = System.getenv(envKey);
        if(hasValue(envValue)){
            return envValue;
        }

        // JVM variable
        String systemValue = System.getProperty(key);
        if(hasValue(systemValue)){
            return systemValue;
        }

        // Properties file
        String propertyValue = PROPERTIES.getProperty(key);
        if(hasValue(propertyValue)){
            return propertyValue;
        }
        throw new IllegalStateException( "Missing or blank configuration for key: " + key
                + ". Set environment variable '" + envKey
                + "', JVM property '-D" + key
                + "=...', or add it to config-"
                + ENVIRONMENT + ".properties");
    }

    private String resolveEnv(){
        String environment = System.getProperty("env");
        if(!hasValue(environment)){
            environment = System.getenv("ENV");
        }if(!hasValue(environment)){
            environment = DEFAULT_ENV;
        }
        LOG.info("resolve env - {}", environment);
        return environment.trim().toLowerCase(Locale.ROOT);
    }

    private Properties loadProperties() {
        LOG.info("env - {}", ENVIRONMENT);
        String fileName = "config-" + ENVIRONMENT + ".properties";
        Properties properties = new Properties();
        LOG.info("Loading configuration file: {}", fileName);

        try(InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(fileName)){
            if (inputStream==null){
                throw new RuntimeException("File not found: " + fileName);
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + fileName, e);
        }
    }

    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

}
