package com.example.currencyconversionservice.controller;

import com.example.currencyconversionservice.domain.CurrencyConversion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "currency-exchange-service")
public interface CurrencyExchangeProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    CurrencyConversion retrieveExchangeValue(@RequestHeader("Authorization") String authorization, @PathVariable String from, @PathVariable String to);
}
