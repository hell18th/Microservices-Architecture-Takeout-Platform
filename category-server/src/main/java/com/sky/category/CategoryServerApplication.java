package com.sky.category;

import com.sky.api.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.sky.api", defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication
public class CategoryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CategoryServerApplication.class, args);
    }

}