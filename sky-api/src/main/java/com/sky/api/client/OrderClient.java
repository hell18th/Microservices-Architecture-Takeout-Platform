package com.sky.api.client;

import com.sky.api.dto.SalesDTO;
import com.sky.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient("order-server")
public interface OrderClient {
    @GetMapping("/admin/order/turnover")
    Result<Double> getTurnover(@RequestParam("dayBegin") LocalDateTime dayBegin, @RequestParam("dayEnd") LocalDateTime dayEnd);

    @GetMapping("/admin/order/countAll")
    Result<Integer> countAll(@RequestParam("dayBegin") LocalDateTime dayBegin, @RequestParam("dayEnd") LocalDateTime dayEnd);

    @GetMapping("/admin/order/countCOMPLETED")
    Result<Integer> countCOMPLETED(@RequestParam("dayBegin") LocalDateTime dayBegin, @RequestParam("dayEnd") LocalDateTime dayEnd);

    @GetMapping("/admin/order/getSales")
    Result<List<SalesDTO>> getSales(@RequestParam("dayBegin") LocalDateTime dayBegin, @RequestParam("dayEnd") LocalDateTime dayEnd);

    @GetMapping("/admin/order/countCONFIRMED")
    Result<Integer> countCONFIRMED(@RequestParam("dayBegin") LocalDateTime dayBegin, @RequestParam("dayEnd") LocalDateTime dayEnd);

    @GetMapping("/admin/order/countDELIVERY")
    Result<Integer> countDELIVERY(@RequestParam("dayBegin") LocalDateTime dayBegin, @RequestParam("dayEnd") LocalDateTime dayEnd);

    @GetMapping("/admin/order/countCANCELLED")
    Result<Integer> countCANCELLED(@RequestParam("dayBegin") LocalDateTime dayBegin, @RequestParam("dayEnd") LocalDateTime dayEnd);
}