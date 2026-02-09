package com.example.apimessage;

import org.springframework.boot.SpringApplication;
import com.example.apimessage.config.TwilioProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(TwilioProperties.class)
@EnableScheduling
public class ApiMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMessageApplication.class, args);
    }
}
