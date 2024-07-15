package com.wifi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WifiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WifiApplication.class, args);
    }
}
