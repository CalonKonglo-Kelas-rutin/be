package com.horolofi.rwa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan(basePackages = "com.horolofi.rwa")
public class HoroloFiRwaApplication  {

    public static void main(String[] args) {
        SpringApplication.run(HoroloFiRwaApplication.class, args);
    }
}
