package com.example.currencyexchangeservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private final Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
//    @Retry(name = "sample-api", fallbackMethod = "hardcodedFallback")
    @CircuitBreaker(name = "sample-api", fallbackMethod = "hardcodedFallback")
    public String sampleApi() {
        logger.info("Sample api call received");
        ResponseEntity<String> restTemplate = new RestTemplate().getForEntity("http://localhost:8080", String.class);
        return "Sample API";
    }

    private String hardcodedFallback(Exception ex) {
        return "Fallback response";
    }
}
