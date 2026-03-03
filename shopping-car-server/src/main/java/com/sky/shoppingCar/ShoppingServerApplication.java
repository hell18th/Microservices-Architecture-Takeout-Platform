package com.sky.shoppingCar;

import com.sky.api.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(value = "com.sky.api", defaultConfiguration = DefaultFeignConfig.class)
@SpringBootApplication
public class ShoppingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingServerApplication.class, args);
    }

}