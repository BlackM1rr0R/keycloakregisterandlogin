package com.example.keycloackspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class KeycloackspringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeycloackspringbootApplication.class, args);
        System.out.println("Working");
    }

}
