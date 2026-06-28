package com.cheongcel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CheongcelApplication {
    public static void main(String[] args) {
        SpringApplication.run(CheongcelApplication.class, args);
    }
}
