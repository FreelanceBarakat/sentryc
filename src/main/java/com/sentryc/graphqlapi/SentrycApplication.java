package com.sentryc.graphqlapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SentrycApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentrycApplication.class, args);
    }

}
