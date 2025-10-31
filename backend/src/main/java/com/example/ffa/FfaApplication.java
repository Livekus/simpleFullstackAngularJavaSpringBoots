package com.example.ffa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FfaApplication {
    public static void main(String[] args) {
        SpringApplication.run(FfaApplication.class, args);
    }
}