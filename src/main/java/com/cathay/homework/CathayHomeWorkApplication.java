package com.cathay.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = { "com.cathay.homework" })
@EnableConfigurationProperties
@EnableJpaAuditing
public class CathayHomeWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(CathayHomeWorkApplication.class, args);
    }
}
