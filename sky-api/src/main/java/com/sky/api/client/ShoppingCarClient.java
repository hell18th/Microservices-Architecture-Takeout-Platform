package com.sky.api.client;

import com.sky.api.dto.ApiShoppingCar;
import com.sky.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient("shopping-car-server")
public interface ShoppingCarClient {
    @GetMapping("/user/shoppingCar/list")
    Result<List<ApiShoppingCar>> list();

    @DeleteMapping("/user/shoppingCar/clean")
    Result<Void> clean();

    @PostMapping("/user/shoppingCar/insertBatch")
    Result<Void> insertBatch(List<ApiShoppingCar> list);
}