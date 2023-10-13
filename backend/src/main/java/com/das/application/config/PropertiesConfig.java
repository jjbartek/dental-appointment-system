package com.das.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "das.app")
public class PropertiesConfig {
    private String jwtSecret;
    private long expirationTime;
    private String url;
    private String version;
    private String description;

}
