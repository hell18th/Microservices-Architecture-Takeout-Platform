package com.sky.api.client;

import com.sky.api.dto.ApiAddress;
import com.sky.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("address-server")
public interface AddressClient {
    @GetMapping("/user/address/{id}")
    Result<ApiAddress> getById(@PathVariable Long id);
}