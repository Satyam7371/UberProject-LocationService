package org.example.uberprojectlocationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient                                // this is to mark this as a eureka client
public class UberProjectLocationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberProjectLocationServiceApplication.class, args);
    }

}
