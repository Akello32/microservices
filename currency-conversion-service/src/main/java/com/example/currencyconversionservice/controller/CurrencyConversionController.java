package com.example.currencyconversionservice.controller;

import com.example.currencyconversionservice.domain.CurrencyConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;

    @GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate()
                .getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);

        CurrencyConversion currencyConversion = responseEntity.getBody();

        return new CurrencyConversion(currencyConversion.getId(), from, to, quantity, currencyConversion.getConversion(),
                quantity.multiply(currencyConversion.getConversion()), currencyConversion.getEnvironment());
    }

    @GetMapping("/feing/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeing(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity,
                                                               @RequestHeader("Authorization") String authorization) {
        CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchangeValue(authorization, from, to);

        return new CurrencyConversion(currencyConversion.getId(), from, to, quantity, currencyConversion.getConversion(),
                quantity.multiply(currencyConversion.getConversion()), currencyConversion.getEnvironment());
    }
}
