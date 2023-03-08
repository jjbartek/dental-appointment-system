package com.das.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "das.app")
public class PropertiesConfig {
    private String jwtSecret;
    private long expirationTime;
}
