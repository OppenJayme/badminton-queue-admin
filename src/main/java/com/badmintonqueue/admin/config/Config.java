package com.badmintonqueue.admin.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties props = new Properties();

    public Config() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException ignored) {
        }
    }

    public String getApiBaseUrl() {
        return props.getProperty("api.baseUrl", "http://localhost:5237");
    }
}
