package com.das;

import com.das.config.PropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PropertiesConfig.class)
public class DASApplication {

    public static void main(String[] args) {
        SpringApplication.run(DASApplication.class, args);
    }

}
