package com.propertymonitor.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private static String environment;

    static {
        try {
            // Load config.properties file
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/config.properties");
            properties = new Properties();
            properties.load(fileInputStream);

            // Read the current environment (stage, pre-release, production)
            environment = System.getProperty("env", properties.getProperty("env", "stage"));
            System.out.println("Current Environment: " + environment);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage());
        }
    }

    /**
     * Get value for the given key, with environment override if available.
     */
    public static String getProperty(String key) {
        String envSpecificValue = properties.getProperty(environment + "." + key);
        if (envSpecificValue != null) {
            return envSpecificValue;
        }
        return properties.getProperty(key); // fallback to global key
    }

    /**
     * Alias for getProperty() to support legacy method names.
     */
    public static String getEnvProperty(String key) {
        return getProperty(key);
    }

    // Convenience getters
    public static String getBaseUrl() {
        return getProperty("baseUrl");
    }

    public static String getProjectSearchUrl() {
        return getProperty("projectSearchUrl");
    }

    public static String getLoginApiUrl() {
        return getProperty("loginApiUrl");
    }

    public static String getApiUsername() {
        return getProperty("login.username");
    }

    public static String getApiPassword() {
        return getProperty("login.password");
    }

    public static String getCaptchaTestValue() {
        return getProperty("captchaTestValue");
    }

    public static String getDeviceId() {
        return getProperty("deviceId");
    }

    public static String getEnvironment() {
        return environment;
    }
}
