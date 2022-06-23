package com.example.currencyexchangeservice.controller;

import com.example.currencyexchangeservice.domain.CurrencyExchange;
import com.example.currencyexchangeservice.repository.CurrencyExchangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/currency-exchange")
public class CurrencyExchangeController {

    private final Environment environment;

    private final CurrencyExchangeRepository currencyExchangeRepository;

    private final Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    public CurrencyExchangeController(Environment environment, CurrencyExchangeRepository currencyExchangeRepository) {
        this.environment = environment;
        this.currencyExchangeRepository = currencyExchangeRepository;
    }

    @GetMapping("/values")
    public Map retrieveAllCurrencyExchange(@RequestHeader Map<String, String> headers) {
        return headers;
    }

    @GetMapping("/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        String env = environment.getProperty("local.server.port");
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to)
                .orElseThrow(() -> new RuntimeException("Unable to find data for " + from + " to " + to));
        currencyExchange.setEnvironment(env);
        return currencyExchange;
    }
}
