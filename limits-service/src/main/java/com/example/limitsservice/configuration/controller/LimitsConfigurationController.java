package com.example.limitsservice.configuration.controller;

import com.example.limitsservice.configuration.Configuration;
import com.example.limitsservice.configuration.domain.LimitsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsConfigurationController {

    @Autowired
    private Configuration configuration;

    @GetMapping("/limits")
    public LimitsConfiguration retrieveLimitsFromConfiguration() {
        return new LimitsConfiguration(configuration.getMaximum(), configuration.getMinimum() );
    }
}
